package cn.net.ziqiang.teamup.backend.util.annotation.user

import io.swagger.v3.oas.annotations.security.SecurityRequirement

/**
 * 需要验证是否为正常用户（用户信息完整、不在审核、不在黑名单内）
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@SecurityRequirement(name = "JWT Auth")
annotation class NormalUser