package cn.net.ziqiang.teamup.backend.web.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AuthenticationFailHandler: AuthenticationFailureHandler,
    AccessDeniedHandler, AuthenticationEntryPoint {
    @Autowired
    private lateinit var handlerExceptionResolver: HandlerExceptionResolver

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        handlerExceptionResolver.resolveException(request, response, null, exception)
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        handlerExceptionResolver.resolveException(request, response, null, authException)
    }
}