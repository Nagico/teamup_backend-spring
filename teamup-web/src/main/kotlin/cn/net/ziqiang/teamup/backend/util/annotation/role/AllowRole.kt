package cn.net.ziqiang.teamup.backend.util.annotation.role

import cn.net.ziqiang.teamup.backend.constant.UserRole

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AllowRole (
    val role: UserRole = UserRole.None
)