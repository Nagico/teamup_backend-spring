package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message
import cn.net.ziqiang.teamup.backend.service.business.MessageBusiness
import cn.net.ziqiang.teamup.backend.service.service.MessageService
import com.alibaba.fastjson.JSONObject
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.MessagingException
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.support.RequestContext

@Service
class MessageServiceImpl : MessageService {
    @Autowired
    private lateinit var messagingTemplate: SimpMessageSendingOperations

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    private lateinit var messageBusiness: MessageBusiness

    override fun deliverToWS(message: String) : Boolean {
        try {
            val msg = JSONObject.parseObject(message, Message::class.java)
            if (msg.receiver == null && msg.type == MessageType.Chat) {
                messagingTemplate.convertAndSend("/topic/public", message)
            } else
            if (msg.receiver != null && msg.type == MessageType.Chat) {
                messagingTemplate.convertAndSendToUser(msg.receiver!!, "/topic/private", message)
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    override fun sendMsg(message: Message) {
        rabbitTemplate.convertAndSend(
            "topicWebSocketExchange",
            "topic.public",
            JSONObject.toJSONString(message)
        )
    }
}