package cn.net.ziqiang.teamup.backend.web.annotation.permission

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class OwnerOrManager(val field: String, val message: String = "")
