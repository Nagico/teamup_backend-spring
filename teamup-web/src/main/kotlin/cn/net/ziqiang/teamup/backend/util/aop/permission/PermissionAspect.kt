package cn.net.ziqiang.teamup.backend.util.aop.permission


import cn.net.ziqiang.teamup.backend.util.annotation.permission.Owner
import cn.net.ziqiang.teamup.backend.util.annotation.permission.OwnerOrManager
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class PermissionAspect {
    @Before("@annotation(cn.net.ziqiang.teamup.backend.util.annotation.permission.Owner)")
    fun verifyOwner(jp: JoinPoint) {
        val annotation = (jp.signature as MethodSignature).method.getAnnotation(Owner::class.java)

        val requestContext = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes

        requestContext.request.setAttribute("checkField", annotation.field)
        requestContext.request.setAttribute("allowManager", false)
        requestContext.request.setAttribute("checkFailedMsg", annotation.message)
        requestContext.request.setAttribute("user", SecurityContextUtils.user)
    }

    @Before("@annotation(cn.net.ziqiang.teamup.backend.util.annotation.permission.OwnerOrManager)")
    fun verifyOwnerOrManager(jp: JoinPoint) {
        val annotation = (jp.signature as MethodSignature).method.getAnnotation(OwnerOrManager::class.java)

        val requestContext = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes

        requestContext.request.setAttribute("checkField", annotation.field)
        requestContext.request.setAttribute("allowManager", true)
        requestContext.request.setAttribute("checkFailedMsg", annotation.message)
        requestContext.request.setAttribute("user", SecurityContextUtils.user)
    }
}