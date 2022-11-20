package cn.net.ziqiang.teamup.backend.web.annotation.role

import cn.net.ziqiang.teamup.backend.common.constant.UserRole

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AllowRoles (
    val roles: Array<UserRole> = [],
    val all: Boolean = false
)