package cn.net.ziqiang.teamup.backend.web.security

import cn.net.ziqiang.teamup.backend.common.bean.auth.JwtPayload
import cn.net.ziqiang.teamup.backend.common.constant.ResultType
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import org.springframework.security.core.context.SecurityContextHolder

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/9/26 21:29
 */
object SecurityContextUtils {

    val jwtPayload: JwtPayload?
        get() = SecurityContextHolder.getContext().authentication.details as? JwtPayload

    private val userId: Long?
        get() = jwtPayload?.userId

    val role: UserRole
        get() = jwtPayload?.role ?: UserRole.None

    val userIdOrThrow: Long
        get() = userId ?: throw ApiException(ResultType.NotLogin)
}