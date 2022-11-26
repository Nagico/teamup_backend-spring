package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.service.service.MessageService
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import java.security.Principal
import javax.annotation.security.PermitAll

@Controller
class MessageController {
    @Autowired
    private lateinit var messageService: MessageService

    /**
     * 接收 客户端传过来的消息 通过setSender和type 来判别时单发还是群发
     * @param message
     */
    @PermitAll
    @MessageMapping("/chat.sendMsg")
    fun sendMessageTest(principal: Principal, @Payload message: Message) {
        message.sender = principal.name
        messageService.sendMsg(message)
    }
}
