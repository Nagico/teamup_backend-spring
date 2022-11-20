package cn.net.ziqiang.teamup.backend.service.service.user.impl

import cn.hutool.core.bean.BeanUtil
import cn.net.ziqiang.teamup.backend.common.constant.ResultType
import cn.net.ziqiang.teamup.backend.common.dto.user.UpdateUserProfileDto
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.service.cache.UserCacheManager
import cn.net.ziqiang.teamup.backend.service.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService{
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userCacheManager: UserCacheManager

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

    override fun updateUser(id: Long, dto: UpdateUserProfileDto) : User {
        val user = getUserById(id = id)
        val copiedUser = User()
        BeanUtil.copyProperties(user, copiedUser)
        copiedUser.apply {
            realName = dto.realName
            username = dto.nickname
            phone = dto.phone
            faculty = dto.faculty
        }
        userRepository.save(copiedUser)
        userCacheManager.setUserCache(copiedUser)
        return copiedUser
    }

    override fun checkNormalUserOrThrow(user: User) {
        if (user.blocked)
            throw ApiException(type = ResultType.UserBlocked, message = "账号已被封禁")
    }
}