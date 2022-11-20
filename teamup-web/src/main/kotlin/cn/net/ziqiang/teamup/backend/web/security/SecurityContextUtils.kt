package cn.net.ziqiang.teamup.backend.web.security

import cn.net.ziqiang.teamup.backend.common.bean.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.common.constant.ResultType
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import org.springframework.security.core.context.SecurityContextHolder


object SecurityContextUtils {
    private val jwtPayload: JwtPayload?
        get() = SecurityContextHolder.getContext().authentication.details as? JwtPayload

    private val userIdOrNull: Long?
        get() = jwtPayload?.userId

    val role: UserRole
        get() = jwtPayload?.role ?: UserRole.None

    val userId: Long
        get() = userIdOrNull ?: throw ApiException(ResultType.NotLogin)
}