package cn.net.ziqiang.teamup.backend.service.service.user.impl

import cn.hutool.core.bean.BeanUtil
import cn.net.ziqiang.teamup.backend.common.constant.FileConstant.DEFAULT_AVATAR
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.dto.user.UpdateUserProfileDto
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.service.business.OssBusiness
import cn.net.ziqiang.teamup.backend.service.cache.UserCacheManager
import cn.net.ziqiang.teamup.backend.service.service.user.UserService
import cn.net.ziqiang.teamup.backend.service.vo.user.UserInfoVO
import cn.net.ziqiang.teamup.backend.service.vo.user.UserProfileVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class UserServiceImpl : UserService{
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userCacheManager: UserCacheManager
    @Autowired
    private lateinit var ossBusiness: OssBusiness

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
        if (dto.realName.isNotEmpty() && dto.username.isNotEmpty() &&
            dto.phone.isNotEmpty() && dto.faculty.isNotEmpty() &&
            dto.grade.isNotEmpty()
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
        if (dto.phone.isNotEmpty() && dto.phone != user.phone)
            copiedUser.phone = dto.phone
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
        if (username == null)
            return 0
        return userRepository.countByUsername(username)
    }
}