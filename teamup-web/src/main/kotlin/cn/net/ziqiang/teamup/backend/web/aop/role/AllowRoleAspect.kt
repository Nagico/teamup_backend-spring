package cn.net.ziqiang.teamup.backend.web.aop.role

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.web.annotation.role.AllowRole
import cn.net.ziqiang.teamup.backend.web.annotation.role.AllowRoles
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class AllowRoleAspect {
    @Around("@annotation(cn.net.ziqiang.teamup.backend.web.annotation.role.AllowRoles)")
    fun verifyRoles(jp: ProceedingJoinPoint): Any? {
        val annotation = (jp.signature as MethodSignature).method.getAnnotation(AllowRoles::class.java)
        val roles = (if (annotation.all) UserRole.all() else annotation.roles).map { it.string }.toSet()
        val httpRequest = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        for (role in roles) {
            if (role.isNotEmpty() && httpRequest.isUserInRole(role)) {
                jp.proceed()
            }
        }

        throw org.springframework.security.access.AccessDeniedException("")
    }

    @Around("@annotation(cn.net.ziqiang.teamup.backend.web.annotation.role.AllowRole)")
    fun verifyRole(jp: ProceedingJoinPoint): Any? {
        val annotation = (jp.signature as MethodSignature).method.getAnnotation(AllowRole::class.java)
        val roles = UserRole.values().filter { it.weight >= annotation.role.weight }.map { it.toString() }
        val httpRequest = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        for (role in roles) {
            if (role.isNotEmpty() && httpRequest.isUserInRole(role)) {
                jp.proceed()
            }
        }

        throw org.springframework.security.access.AccessDeniedException("")
    }
}