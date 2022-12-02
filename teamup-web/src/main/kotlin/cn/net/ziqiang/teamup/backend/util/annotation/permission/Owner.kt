package cn.net.ziqiang.teamup.backend.util.annotation.permission

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Owner(val field: String, val message: String = "")
