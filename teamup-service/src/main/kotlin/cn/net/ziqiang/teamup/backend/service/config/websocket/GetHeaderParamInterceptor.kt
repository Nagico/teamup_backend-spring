package cn.net.ziqiang.teamup.backend.service.config.websocket

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
import java.util.*

@Slf4j
@Component
class GetHeaderParamInterceptor : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
        val accessor = MessageHeaderAccessor.getAccessor(
            message,
            StompHeaderAccessor::class.java
        )
        if (StompCommand.CONNECT == accessor!!.command) {
            val raw = message.headers[SimpMessageHeaderAccessor.NATIVE_HEADERS]
            if (raw is Map<*, *>) {
                val name = raw["username"] //取出客户端携带的参数
                logger.info("user login: $name")
                if (name is LinkedList<*>) {
                    // 设置当前访问的认证用户
                    accessor.user = UserPrincipal(name[0].toString())
                }
            }
        }
        return message
    }
}