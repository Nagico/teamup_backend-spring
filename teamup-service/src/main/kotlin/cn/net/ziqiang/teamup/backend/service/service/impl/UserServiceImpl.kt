package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.hutool.core.bean.BeanUtil
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.constant.FileConstant.DEFAULT_AVATAR
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.constant.status.UserStatus
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.RegisterDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.ResetPasswordDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.*
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.service.business.OssBusiness
import cn.net.ziqiang.teamup.backend.service.cache.UserCacheManager
import cn.net.ziqiang.teamup.backend.service.service.UserService
import cn.net.ziqiang.teamup.backend.common.util.SecurityUtils
import cn.net.ziqiang.teamup.backend.service.service.AuthService
import cn.net.ziqiang.teamup.backend.service.service.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class UserServiceImpl : UserService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userCacheManager: UserCacheManager
    @Autowired
    private lateinit var ossBusiness: OssBusiness
    @Autowired
    private lateinit var smsService: SmsService
    @Autowired
    private lateinit var authService: AuthService

    override fun getUserById(id: Long): User {
        return getUserByIdOrNull(id) ?: throw ApiException(type = ResultType.ResourceNotFound, message = "用户不存在")
    }

    override fun getUserByIdOrNull(id: Long): User? {
        return userCacheManager.getUserCache(userId = id) ?: run {
            val query = userRepository.findById(id).orElse(null)
            query?.let {
                userCacheManager.setUserCache(user = query)
            }
            query
        }
    }

    override fun getUserInfoById(id: Long): UserInfoVO {
        val user = getUserById(id)
        if (!user.active)
            throw ApiException(type = ResultType.ResourceNotFound, message = "用户不存在")
        return UserInfoVO(user)
    }

    override fun getUserProfileById(id: Long): UserProfileVO {
        val user = getUserById(id)

        return UserProfileVO(user)
    }

    override fun updateUser(id: Long, dto: UpdateUserProfileDto) : UserProfileVO {
        val user = getUserById(id = id)
        val copiedUser = User()
        BeanUtil.copyProperties(user, copiedUser)

        // 检测激活
        if (dto.realName.isNotEmpty() && dto.faculty.isNotEmpty() && dto.grade.isNotEmpty() && dto.username.isNotEmpty()
        ) {
            copiedUser.active = true
        } else {
            copiedUser.active = user.active
        }

        // 更新
        if (dto.realName.isNotEmpty() && dto.realName != user.realName)
            copiedUser.realName = dto.realName
        if (dto.username.isNotEmpty() && dto.username != user.username) {
            copiedUser.username = dto.username
            if (countByUsername(dto.username) > 0)  // 检测重复
                throw ApiException(type = ResultType.ParamValidationFailed, message = "用户名已存在")
        }
        if (dto.faculty.isNotEmpty() && dto.faculty != user.faculty)
            copiedUser.faculty = dto.faculty
        if (dto.grade.isNotEmpty() && dto.grade != user.grade)
            copiedUser.grade = dto.grade
        userRepository.save(copiedUser)
        userCacheManager.setUserCache(copiedUser)

        return UserProfileVO(copiedUser)
    }

    @Transactional
    override fun updateUserAvatar(id: Long, avatar: MultipartFile): UserProfileVO {
        if (avatar.isEmpty)
            throw ApiException(type = ResultType.ParamValidationFailed, message = "头像不能为空")
        if (avatar.contentType != "image/jpeg" && avatar.contentType != "image/png")
            throw ApiException(type = ResultType.ParamValidationFailed, message = "头像仅支持jpg与png格式的图片")

        val user = getUserById(id)

        if (!user.avatar.isNullOrEmpty() && user.avatar != DEFAULT_AVATAR) {
            ossBusiness.deleteFileByUrl(user.avatar!!)
        }

        user.avatar = ossBusiness.uploadAvatar(avatar)
        userRepository.save(user)
        userCacheManager.setUserCache(user)

        return UserProfileVO(user)
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

    override fun register(registerDto: RegisterDto): TokenBean {
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

        logger.info("新用户: $newUser")

        //返回新的token
        return authService.generateToken(newUser)
    }

    override fun resetPassword(resetPasswordDto: ResetPasswordDto) {
        if (!smsService.checkVerifyCode(phone = resetPasswordDto.phone, code = resetPasswordDto.verifyCode)) {
            throw ApiException(ResultType.ParamValidationFailed, "验证码错误")
        }

        val user = userRepository.findByPhone(phone = resetPasswordDto.phone)
            ?: throw ApiException(ResultType.ParamValidationFailed, "手机号不存在")

        user.password = SecurityUtils.encryptPassword(password = resetPasswordDto.password)
        userRepository.save(user)
    }

    override fun changePhone(userId: Long, changePhoneDto: ChangePhoneDto) {
        val user = getUserById(userId)

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
            userCacheManager.setUserStatusCache(userId, UserStatus.Online)
        }
    }

    override fun messageLogout(user: User) {
        logger.info("user logout: ${user.id}, ${user.username}")
        userCacheManager.setUserStatusCache(user.id!!, UserStatus.Offline)
    }

    override fun getUserStatus(userId: Long): UserStatus {
        return userCacheManager.getUserStatusCache(userId)
    }

    override fun changePassword(userId: Long, changePasswordDto: ChangePasswordDto) {
        val user = userRepository.findById(userId).orElseThrow { ApiException(ResultType.ResourceNotFound, "用户不存在") }

        if (!SecurityUtils.matches(password = changePasswordDto.oldPassword, encodedPassword = user.password)) {
            throw ApiException(ResultType.PasswordWrong)
        }

        user.password = SecurityUtils.encryptPassword(password = changePasswordDto.newPassword)
        userRepository.save(user)
        userCacheManager.setUserCache(user)
    }

}