package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.constant.status.UserStatus
import cn.net.ziqiang.teamup.backend.pojo.entity.Message
import cn.net.ziqiang.teamup.backend.dao.repository.MessageRepository
import cn.net.ziqiang.teamup.backend.service.MessageService
import cn.net.ziqiang.teamup.backend.service.UserService
import com.alibaba.fastjson.JSONObject
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.MessagingException
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.concurrent.thread

@Slf4j
@Service
class MessageServiceImpl : MessageService {
    @Autowired
    private lateinit var messagingTemplate: SimpMessageSendingOperations
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var messageRepository: MessageRepository

    override fun deliver(message: Message) {
        if (message.receiver != null) {
            if (userService.getUserStatus(message.receiver!!) == UserStatus.Online) {
                deliverToUser(message)
                // return
            }
            thread {
                if (userService.getUserByIdOrNull(message.receiver!!) != null) {
                    messageRepository.save(message)
                    // logger.debug("User {} is offline, save message to database", message.receiver)
                }
            }
            return
        }

        deliverToAll(message)
    }

    override fun deliver(message: String) : Boolean {
        return try {
            val msg = JSONObject.parseObject(message, Message::class.java)
            deliver(msg)
            true
        } catch (e: MessagingException) {
            logger.error("deliver message failed: $message")
            logger.info(e.stackTraceToString())
            false
        }
    }

    override fun sendMsg(senderId: Long, message: Message) {
        message.sender = senderId
        sendToMQ(message)
    }

    @Transactional
    override fun getOfflineMsg(receiver: Long): List<Message> {
        val messages = messageRepository.findAllByReceiverOrderByCreateTime(receiver)
        // messageRepository.deleteAllByReceiver(receiver)

        return messages
    }

    // region Utils
    private fun sendToMQ(message: Message) {
        rabbitTemplate.convertAndSend(
            "topicWebSocketExchange",
            "topic.public",
            JSONObject.toJSONString(message)
        )
    }

    private fun deliverToAll(message: Message) {
        messagingTemplate.convertAndSend(
            "/topic/public",
            JSONObject.toJSONString(message)
        )
    }

    private fun deliverToUser(message: Message) {
        messagingTemplate.convertAndSendToUser(
            message.receiver.toString(),
            "/topic/private",
            JSONObject.toJSONString(message)
        )
    }
    // endregion
}