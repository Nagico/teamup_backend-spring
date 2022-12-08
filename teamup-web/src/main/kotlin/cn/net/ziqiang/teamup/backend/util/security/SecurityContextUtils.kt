package cn.net.ziqiang.teamup.backend.util.security

import cn.net.ziqiang.teamup.backend.pojo.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.constant.UserRole
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityContextUtils {
    @Autowired
    fun setService(service: UserService) {
        userService = service
    }

    companion object {
        private lateinit var userService: UserService

        private val jwtPayload: JwtPayload?
            get() = SecurityContextHolder.getContext().authentication.details as? JwtPayload

        private val userIdOrNull: Long?
            get() = jwtPayload?.userId

        val role: UserRole
            get() = jwtPayload?.role ?: UserRole.None

        val userId: Long
            get() = userIdOrNull ?: throw ApiException(ResultType.NotLogin)

        val user: User
            get() = userService.getUserById(userId)

        val userOrNull: User?
            get() {
                try {
                    return userIdOrNull?.let { userService.getUserById(it) }
                }
                catch (_: Exception) {
                }
                return null
            }
    }
}