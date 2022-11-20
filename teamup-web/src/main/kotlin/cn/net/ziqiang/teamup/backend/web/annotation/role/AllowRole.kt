package cn.net.ziqiang.teamup.backend.web.annotation.role

import cn.net.ziqiang.teamup.backend.common.constant.UserRole

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AllowRole (
    val role: UserRole = UserRole.None
)