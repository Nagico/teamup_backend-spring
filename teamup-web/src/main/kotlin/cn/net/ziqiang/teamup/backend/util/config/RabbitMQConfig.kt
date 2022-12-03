package cn.net.ziqiang.teamup.backend.util.config

import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.util.properties.RabbitMqProperties
import cn.net.ziqiang.teamup.backend.service.MessageService
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy


@Slf4j
@Configuration
class RabbitMQConfig {
    @Lazy
    @Autowired
    private lateinit var messageService: MessageService
    @Autowired
    private lateinit var rabbitMqProperties: RabbitMqProperties

    @Bean
    fun topicQueue(): Queue {
        return Queue(msgTopicQueue, true)
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange("topicWebSocketExchange", true, false)
    }

    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    fun bindingExchangeMessage(): Binding {
        return BindingBuilder.bind(topicQueue()).to(exchange()).with(msgTopicKey)
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory(rabbitMqProperties.host, rabbitMqProperties.port)
        connectionFactory.username = rabbitMqProperties.username
        connectionFactory.setPassword(rabbitMqProperties.password)
        connectionFactory.virtualHost = rabbitMqProperties.virtualHost
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED)
        // connectionFactory.isPublisherConfirms = true // 发送消息回调,必须要设置
        connectionFactory.isPublisherReturns = true
        return connectionFactory
    }

    @Bean
    fun createRabbitTemplate(connectionFactory: ConnectionFactory?): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate()
        rabbitTemplate.connectionFactory = connectionFactory!!
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true)
        rabbitTemplate.setConfirmCallback { correlationData, ack, cause ->
            logger.debug("ConfirmCallback:     相关数据：$correlationData")
            logger.debug("ConfirmCallback:     确认情况：$ack")
            logger.debug("ConfirmCallback:     原因：$cause")
        }

        rabbitTemplate.setReturnsCallback { message ->
            logger.debug("ReturnCallback:     消息：${message.message}")
            logger.debug("ReturnCallback:     回应码：${message.replyCode}")
            logger.debug("ReturnCallback:     回应信息：${message.replyText}")
            logger.debug("ReturnCallback:     交换机：${message.exchange}")
            logger.debug("ReturnCallback:     路由键：${message.routingKey}")
        }
        return rabbitTemplate
    }

    /**
     * 接受消息的监听，这个监听会接受消息队列topicQueue的消息
     * 针对消费者配置
     * @return
     */
    @Bean
    fun messageContainer(): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer(connectionFactory())
        container.setQueues(topicQueue())
        container.isExposeListenerChannel = true
        container.setMaxConcurrentConsumers(1)
        container.setConcurrentConsumers(1)
        container.acknowledgeMode = AcknowledgeMode.MANUAL //设置确认模式手工确认
        container.setMessageListener(ChannelAwareMessageListener { message, channel ->
            val body = message.body
            val msg = String(body)
            logger.info("rabbitmq收到消息 : $msg")
            val sendToWebsocket = messageService.deliver(msg)
            if (sendToWebsocket) {
                logger.debug("消息处理成功！ 已经推送到websocket！")
                channel.basicAck(message.messageProperties.deliveryTag, true) //确认消息成功消费
            }
        })
        return container
    }

    companion object {
        //绑定键
        const val msgTopicKey = "topic.public"

        //队列
        const val msgTopicQueue = "topicQueue"
    }
}