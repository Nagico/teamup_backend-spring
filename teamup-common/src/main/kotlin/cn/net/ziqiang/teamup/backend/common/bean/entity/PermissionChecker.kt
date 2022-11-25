package cn.net.ziqiang.teamup.backend.common.bean.entity

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

abstract class PermissionChecker<T>(private val field: String, private val foreignAttr: String = "user") {
    fun checkPermission(): T {
        val requestContext = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes

        val checkField = (requestContext.request.getAttribute("checkField") ?: return this as T) as String
        if (checkField != field) {  // field不同，无需检查权限
            return this as T
        }

        val allowManager = (requestContext.request.getAttribute("allowManager") ?: false) as Boolean
        val currentUser = requestContext.request.getAttribute("user") as User
        if (allowManager && currentUser.role == UserRole.Manager) {  // 允许管理员访问时直接通过检查
            return this as T
        }

        val message = (requestContext.request.getAttribute("checkFailedMsg") ?: "") as String

        val field = this::class.java.getDeclaredField(foreignAttr)
        field.isAccessible = true
        val owner = field.get(this) as User

        if (owner.id != currentUser.id) {
            if (message.isNotEmpty()) {
                throw ApiException(ResultType.ResourceNotFound, message)
            } else {
                throw ApiException(ResultType.ResourceNotFound)
            }
        }

        return this as T
    }
}