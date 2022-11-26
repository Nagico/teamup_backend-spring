package cn.net.ziqiang.teamup.backend.service.config.websocket

import cn.net.ziqiang.teamup.backend.service.properties.RabbitMqProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    @Autowired
    private lateinit var getHeaderParamInterceptor: GetHeaderParamInterceptor
    @Autowired
    private lateinit var rabbitMqProperties: RabbitMqProperties

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {


        // 使用RabbitMQ做为消息代理，替换默认的Simple Broker
        //定义了服务端接收地址的前缀，也即客户端给服务端发消息的地址前缀,@SendTo(XXX) 也可以重定向
        registry.setUserDestinationPrefix("/user") //这是给sendToUser使用,前端订阅需要加上/user
        registry.setApplicationDestinationPrefixes("/app") //这是给客户端推送消息到服务器使用 ，推送的接口加上/app
        // "STOMP broker relay"处理所有消息将消息发送到外部的消息代理
        registry.enableStompBrokerRelay("/exchange", "/topic", "/queue", "/amq/queue")
            .setVirtualHost(rabbitMqProperties.virtualHost) //对应自己rabbitmq里的虚拟host
            .setRelayHost(rabbitMqProperties.host)
            .setClientLogin(rabbitMqProperties.username)
            .setClientPasscode(rabbitMqProperties.password)
            .setSystemLogin(rabbitMqProperties.username)
            .setSystemPasscode(rabbitMqProperties.password)
            .setSystemHeartbeatSendInterval(5000)
            .setSystemHeartbeatReceiveInterval(4000)
    }

    /**
     * 采用自定义拦截器，获取connect时候传递的参数
     *
     * @param registration
     */
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(getHeaderParamInterceptor)
    }
}