package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message
import cn.net.ziqiang.teamup.backend.common.pojo.vo.message.MessageVO
import cn.net.ziqiang.teamup.backend.service.service.MessageService
import com.alibaba.fastjson.JSONObject
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.MessagingException
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl : MessageService {
    @Autowired
    private lateinit var messagingTemplate: SimpMessageSendingOperations

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    override fun deliverToUser(message: String) : Boolean {
        try {
            val msg = JSONObject.parseObject(message, Message::class.java)
            if (msg.receiver == null && msg.type == MessageType.Chat) {
                messagingTemplate.convertAndSend("/topic/public", message)
            } else
            if (msg.receiver != null && msg.type == MessageType.Chat) {
                messagingTemplate.convertAndSendToUser(msg.receiver.toString(), "/topic/private", message)
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    override fun sendMsg(senderId: Long, vo: MessageVO) {
        val message = Message(
            id = vo.id,
            type = vo.type,
            content = vo.content,
            sender = senderId,
            receiver = vo.receiver
        )

        rabbitTemplate.convertAndSend(
            "topicWebSocketExchange",
            "topic.public",
            JSONObject.toJSONString(message)
        )
    }
}