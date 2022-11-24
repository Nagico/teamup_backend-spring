package cn.net.ziqiang.teamup.backend.service.util

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object PermissionUtil {
    fun checkPermission(field: String, obj: Any) {
        val requestContext = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes

        val checkField = (requestContext.request.getAttribute("checkField") ?: return) as String
        if (checkField != field) {  // field不同，无需检查权限
            return
        }

        val allowManager = (requestContext.request.getAttribute("allowManager") ?: false) as Boolean
        val currentUser = requestContext.request.getAttribute("user") as User
        if (allowManager && currentUser.role == UserRole.Manager) {  // 允许管理员访问时直接通过检查
            return
        }

        val message = (requestContext.request.getAttribute("checkFailedMsg") ?: "") as String

        val field = obj::class.java.getDeclaredField("user")
        field.isAccessible = true
        val owner = field.get(obj) as User

        if (owner.id != currentUser.id) {
            if (message.isNotEmpty()) {
                throw ApiException(ResultType.ResourceNotFound, message)
            } else {
                throw ApiException(ResultType.ResourceNotFound)
            }
        }
    }
}