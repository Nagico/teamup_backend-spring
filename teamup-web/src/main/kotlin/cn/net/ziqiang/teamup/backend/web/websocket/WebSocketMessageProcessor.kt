package cn.net.ziqiang.teamup.backend.web.websocket

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.util.JwtUtils
import cn.net.ziqiang.teamup.backend.service.properties.JwtProperties
import cn.net.ziqiang.teamup.backend.service.service.MessageService
import cn.net.ziqiang.teamup.backend.service.service.UserService
import cn.net.ziqiang.teamup.backend.web.security.JwtAuthenticationToken
import org.apache.logging.log4j.util.Strings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Slf4j
@Component
class WebSocketMessageProcessor : ChannelInterceptor {
    @Autowired
    private lateinit var jwtProperties: JwtProperties
    @Autowired
    private lateinit var userService: UserService

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(
            message,
            StompHeaderAccessor::class.java
        )
        if (accessor!!.command == StompCommand.CONNECT) {
            val raw = message.headers[SimpMessageHeaderAccessor.NATIVE_HEADERS]
            if (raw is Map<*, *>) {
                try {
                    val header = (raw[HttpHeaders.AUTHORIZATION] as List<String>)[0] //取出客户端携带的参数
                    if (Strings.isEmpty(header) || !header.startsWith("Bearer ") && !header.startsWith("bearer ")) {
                        throw ApiException(ResultType.NotLogin)
                    }

                    val jwtPayloadInfo = JwtUtils.parseJwtWithoutThrow(jwtProperties.secret, header.substring(7))
                    val jwtPayload = jwtPayloadInfo.first ?: throw ApiException(ResultType.TokenInvalid)
                    SecurityContextHolder.getContext().authentication =
                        JwtAuthenticationToken(authorities = AuthorityUtils.createAuthorityList("ROLE_${jwtPayload.role.name}")).apply {
                            details = jwtPayload
                        }
                    accessor.user = userService.messageLogin(jwtPayload.userId)
                }
                catch (e: Exception) {
                    throw ApiException(ResultType.NotLogin)
                }
            }
        }

        if (accessor.command == StompCommand.DISCONNECT) {
            (accessor.user as? User)?.let { userService.messageLogout(it) }
        }
        return message
    }
}