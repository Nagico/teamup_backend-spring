package cn.net.ziqiang.teamup.backend.web.annotation.user

import io.swagger.v3.oas.annotations.security.SecurityRequirement

/**
 * 需要验证是否为激活用户
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@SecurityRequirement(name = "JWT Auth")
annotation class ActiveUser