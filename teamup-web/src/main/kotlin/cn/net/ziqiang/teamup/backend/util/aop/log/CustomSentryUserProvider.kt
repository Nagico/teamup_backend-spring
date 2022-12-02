package cn.net.ziqiang.teamup.backend.util.aop.log

import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import org.springframework.stereotype.Component
import io.sentry.protocol.User
import io.sentry.spring.SentryUserProvider

@Component
class CustomSentryUserProvider : SentryUserProvider {
    override fun provideUser(): User? {
        val user = SecurityContextUtils.userOrNull ?: return null

        return User().apply {
            id = user.id.toString()
            username = user.username
            data = mapOf(
                "name" to user.realName,
                "student_id" to user.studentId.toString(),
                "phone" to user.phone.toString(),
                "faculty" to user.faculty.toString(),
                "role" to user.role.toString(),
                "openid" to user.openid,
                "active" to user.active.toString(),
            )
        }
    }
}