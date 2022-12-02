package cn.net.ziqiang.teamup.backend.util.config

import org.apache.coyote.http11.Http11NioProtocol
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@Component
class WebServerConfig : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory) {
        //使用对应工厂类定制化tomcat connector
        (factory as TomcatServletWebServerFactory).addConnectorCustomizers(TomcatConnectorCustomizer { connector ->
            val protocol = connector.protocolHandler as Http11NioProtocol
            //定制化keepalivetimeout，30秒
            protocol.keepAliveTimeout = 30000
            //客户端发送超过10000个请求自动断开
            protocol.maxKeepAliveRequests = 10000
        })
    }
}