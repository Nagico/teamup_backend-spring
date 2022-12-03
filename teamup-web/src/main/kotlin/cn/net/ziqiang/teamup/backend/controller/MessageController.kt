package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.pojo.entity.Message
import cn.net.ziqiang.teamup.backend.service.MessageService
import cn.net.ziqiang.teamup.backend.util.annotation.user.NormalUser
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.annotation.security.PermitAll
import javax.validation.Valid

@RestController
@RequestMapping("/messages")
class MessageController {
    @Autowired
    private lateinit var messageService: MessageService

    @NormalUser
    @GetMapping("/offline")
    fun getMessages() : List<Message> {
        val user = SecurityContextUtils.user
        return messageService.getOfflineMsg(user.id!!)
    }


    /**
     * 接收 客户端传过来的消息
     * @param message
     */
    @PermitAll
    @MessageMapping("/send")
    fun sendMessageTest(principal: Principal,@Valid @Payload message: Message) : String {
        messageService.sendMsg(principal.name.toLong(), message)

        return "success"
    }
}
