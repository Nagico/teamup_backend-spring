package cn.net.ziqiang.teamup.backend.web.aop.user

import cn.net.ziqiang.teamup.backend.service.service.UserService
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Aspect
@Component
class ActiveUserAspect{
    @Autowired
    private lateinit var userService: UserService

    @Around("@annotation(cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser)")
    fun verifyNormalUser(jp: ProceedingJoinPoint): Any? {
        val userId = SecurityContextUtils.userId
        val user = userService.getUserById(id = userId)
        userService.checkActiveUserOrThrow(user)
        return jp.proceed()
    }
}