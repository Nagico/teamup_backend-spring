package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pojo.vo.message.MessageVO
import cn.net.ziqiang.teamup.backend.service.service.MessageService
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
     * 接收 客户端传过来的消息
     * @param message
     */
    @PermitAll
    @MessageMapping("/chat.sendMsg")
    fun sendMessageTest(principal: Principal, @Payload message: MessageVO) : String {
        messageService.sendMsg(principal.name.toLong(), message)

        return "success"
    }
}
