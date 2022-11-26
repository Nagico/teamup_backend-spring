package cn.net.ziqiang.teamup.backend.web.config.websocket

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Slf4j
@Component
class WebSocketMessageProcessor : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(
            message,
            StompHeaderAccessor::class.java
        )
        if (StompCommand.CONNECT == accessor!!.command) {
            val raw = message.headers[SimpMessageHeaderAccessor.NATIVE_HEADERS]
            if (raw is Map<*, *>) {
                val name = raw["username"] as List<String> //取出客户端携带的参数
                logger.info("user login: $name")
                accessor.user = UserPrincipal(name[0])
            }
        }

        if (StompCommand.DISCONNECT == accessor.command) {
            val name = accessor.user?.name
            logger.info("user logout: $name")
        }
        return message
    }
}