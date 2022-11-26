package cn.net.ziqiang.teamup.backend.web.security


import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.util.JwtUtils
import cn.net.ziqiang.teamup.backend.service.service.AuthService
import org.apache.logging.log4j.util.Strings
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val jwtSecret: String,
    private val authService: AuthService
): BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        SecurityContextHolder.getContext().authentication = null

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (Strings.isEmpty(header) || !header.startsWith("Bearer ") && !header.startsWith("bearer ")) {
            chain.doFilter(request, response)
            return
        }

        val authorization = header.substring(7)

        val jwtPayloadInfo = JwtUtils.parseJwtWithoutThrow(secretKey = jwtSecret, jwtStr = authorization)
        val jwtPayload = jwtPayloadInfo.first ?: run {
            chain.doFilter(request, response)
            return
        }

        if (jwtPayloadInfo.second?.code == ResultType.TokenInvalid.code) {
            //免redis检测时间过了
            authService.getAuthToken(userId = jwtPayload.userId) ?: run {
                chain.doFilter(request, response)
                return
            }
        }

        //认证成功，准备权限
        SecurityContextHolder.getContext().authentication =
            JwtAuthenticationToken(authorities = AuthorityUtils.createAuthorityList("ROLE_${jwtPayload.role.name}")).apply {
                details = jwtPayload
            }
        chain.doFilter(request, response)
    }

}