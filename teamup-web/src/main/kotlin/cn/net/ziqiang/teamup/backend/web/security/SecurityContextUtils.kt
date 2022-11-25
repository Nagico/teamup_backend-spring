package cn.net.ziqiang.teamup.backend.web.security

import cn.net.ziqiang.teamup.backend.common.pojo.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.service.UserService
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
            get() = userIdOrNull?.let { userService.getUserById(it) }
    }
}