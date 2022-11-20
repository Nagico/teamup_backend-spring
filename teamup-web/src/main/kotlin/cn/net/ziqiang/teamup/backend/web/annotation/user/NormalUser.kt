package cn.net.ziqiang.teamup.backend.web.annotation.user

/**
 * 需要验证是否为正常用户（用户信息完整、不在审核、不在黑名单内）
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NormalUser