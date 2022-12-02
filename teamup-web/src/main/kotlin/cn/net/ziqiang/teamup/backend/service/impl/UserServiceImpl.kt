package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.constant.FileConstant.DEFAULT_AVATAR
import cn.net.ziqiang.teamup.backend.constant.UserRole
import cn.net.ziqiang.teamup.backend.constant.status.UserStatus
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.pojo.vo.user.*
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.business.OssBusiness
import cn.net.ziqiang.teamup.backend.cache.UserCacheManager
import cn.net.ziqiang.teamup.backend.service.UserService
import cn.net.ziqiang.teamup.backend.util.SecurityUtils
import cn.net.ziqiang.teamup.backend.util.getInfo
import cn.net.ziqiang.teamup.backend.service.AuthService
import cn.net.ziqiang.teamup.backend.service.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*
import kotlin.concurrent.thread

@Slf4j
@Service
class UserServiceImpl : cn.net.ziqiang.teamup.backend.service.UserService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userCacheManager: UserCacheManager
    @Autowired
    private lateinit var ossBusiness: OssBusiness
    @Autowired
    private lateinit var smsService: cn.net.ziqiang.teamup.backend.service.SmsService
    @Autowired
    private lateinit var authService: cn.net.ziqiang.teamup.backend.service.AuthService

    override fun getUserById(id: Long, useCache: Boolean): User {
        return getUserByIdOrNull(id, useCache) ?: throw ApiException(type = ResultType.ResourceNotFound, message = "用户不存在")
    }

    override fun getUserByIdOrNull(id: Long, useCache: Boolean): User? {
        return if (useCache) {
            userCacheManager.getUserCache(userId = id) ?: run {
                val query = userRepository.findById(id).orElse(null)
                query?.let {
                    userCacheManager.setUserCache(user = query)
                }
                query
            }
        } else {
            userRepository.findById(id).orElse(null)
        }
    }

    override fun getUserInfoById(id: Long): User {
        val user = getUserById(id)
        if (!user.active)
            throw ApiException(type = ResultType.ResourceNotFound, message = "用户不存在")
        return user.apply {
            status = userCacheManager.getUserStatusCache(userId = id)
            getInfo()
        }
    }

    override fun getUserProfileById(id: Long): User {
        return getUserById(id)
    }

    override fun updateUser(id: Long, dto: User) : User {
        val user = getUserById(id = id, useCache = false)

        // 检测激活
        if (!dto.realName.isNullOrBlank() && !dto.faculty.isNullOrBlank() &&
            !dto.grade.isNullOrBlank() && !dto.username.isNullOrBlank()
        ) {
            user.active = true
        }

        // 更新
        dto.realName.let { user.realName = it }
        dto.username.let {
            if (it != user.username && countByUsername(it) > 0)  // 检测重复
                throw ApiException(type = ResultType.ParamValidationFailed, message = "用户名已存在")
            user.username = it
        }
        dto.faculty?.let { user.faculty = it }
        dto.grade?.let { user.grade = it }
        dto.studentId?.let { user.studentId = it }
        dto.introduction?.let { user.introduction = it }

        userRepository.save(user)
        userCacheManager.setUserCache(user)

        return user
    }

    @Transactional
    override fun updateUserAvatar(id: Long, avatar: MultipartFile): User {
        if (avatar.isEmpty)
            throw ApiException(type = ResultType.ParamValidationFailed, message = "头像不能为空")
        if (avatar.contentType != "image/jpeg" && avatar.contentType != "image/png")
            throw ApiException(type = ResultType.ParamValidationFailed, message = "头像仅支持jpg与png格式的图片")

        val user = getUserById(id, useCache = false)

        if (!user.avatar.isNullOrEmpty() && user.avatar != DEFAULT_AVATAR) {
            ossBusiness.deleteFileByUrl(user.avatar!!)
        }

        user.avatar = ossBusiness.uploadAvatar(avatar)
        userRepository.save(user)
        userCacheManager.setUserCache(user)

        return user
    }

    override fun checkNormalUserOrThrow(user: User) {
        if (user.blocked)
            throw ApiException(type = ResultType.UserBlocked, message = "账号已被封禁")
    }

    override fun checkActiveUserOrThrow(user: User) {
        checkNormalUserOrThrow(user)

        if (!user.active)
            throw ApiException(type = ResultType.NotActive, message = "账号未激活")
    }

    override fun countByUsername(username: String?): Int {
        if (username.isNullOrEmpty())
            return -1
        return userRepository.countByUsername(username)
    }

    override fun countByPhone(phone: String?): Int {
        if (phone.isNullOrEmpty())
            return -1
        return userRepository.countByPhone(phone)
    }

    override fun register(registerDto: UserDto): cn.net.ziqiang.teamup.backend.pojo.auth.TokenBean {
        if (!smsService.checkVerifyCode(phone = registerDto.phone, code = registerDto.verifyCode)) {
            throw ApiException(ResultType.ParamValidationFailed, "验证码错误")
        }

        if (userRepository.findByPhone(phone = registerDto.phone) != null) {
            throw ApiException(ResultType.ParamValidationFailed, "手机号已存在")
        }

        val newUser = User(
            username = "",
            password = SecurityUtils.encryptPassword(password = registerDto.password),
            phone = registerDto.phone,
            role = UserRole.User,
            avatar = DEFAULT_AVATAR,
            active = false,
            blocked = false,
            createTime = Date(),
        )

        //更新资料
        userRepository.save(newUser)
        userCacheManager.setUserCache(newUser)

        logger.info("新用户: $newUser")

        //返回新的token
        return authService.generateToken(newUser)
    }

    override fun resetPassword(resetPasswordDto: UserDto) {
        if (!smsService.checkVerifyCode(phone = resetPasswordDto.phone, code = resetPasswordDto.verifyCode)) {
            throw ApiException(ResultType.ParamValidationFailed, "验证码错误")
        }

        val user = userRepository.findByPhone(phone = resetPasswordDto.phone)
            ?: throw ApiException(ResultType.ParamValidationFailed, "手机号不存在")

        user.password = SecurityUtils.encryptPassword(password = resetPasswordDto.password)
        userRepository.save(user)
        userCacheManager.setUserCache(user)
    }

    override fun changePhone(userId: Long, changePhoneDto: UserDto) {
        val user = getUserById(userId, useCache = false)

        if (userRepository.findByPhone(changePhoneDto.phone) != null)
            throw ApiException(type = ResultType.ParamValidationFailed, message = "手机号已被注册")
        if (!smsService.checkVerifyCode(changePhoneDto.phone, changePhoneDto.verifyCode)) {
            throw ApiException(type = ResultType.ParamValidationFailed, message = "验证码错误")
        }

        user.phone = changePhoneDto.phone
        userRepository.save(user)
        userCacheManager.setUserCache(user)
    }

    override fun messageLogin(userId: Long) : User {
        return getUserById(userId).apply {
            logger.info("user login: $userId, $username")
            thread {
                lastLogin = Date()
                userRepository.save(this)
                userCacheManager.setUserCache(this)
                userCacheManager.setUserStatusCache(userId, UserStatus.Online)
            }
        }
    }

    override fun messageLogout(user: User) {
        logger.info("user logout: ${user.id}, ${user.username}")
        thread {
            user.lastLogin = Date()
            userRepository.save(user)
            userCacheManager.setUserCache(user)
            userCacheManager.setUserStatusCache(user.id!!, UserStatus.Offline)
        }

    }

    override fun getUserStatus(userId: Long): UserStatus {
        return userCacheManager.getUserStatusCache(userId)
    }

    override fun changePassword(userId: Long, changePasswordDto: UserDto) {
        val user = getUserById(userId, useCache = false)

        if (!SecurityUtils.matches(password = changePasswordDto.oldPassword, encodedPassword = user.password)) {
            throw ApiException(ResultType.PasswordWrong)
        }

        user.password = SecurityUtils.encryptPassword(password = changePasswordDto.password)
        userRepository.save(user)
        userCacheManager.setUserCache(user)
    }

}