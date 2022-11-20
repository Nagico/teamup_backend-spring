package cn.net.ziqiang.teamup.backend.web.annotation.user

/**
 * 需要验证是否为激活用户
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ActiveUser