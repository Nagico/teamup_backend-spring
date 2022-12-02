package cn.net.ziqiang.teamup.backend.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Component
@Validated
@ConfigurationProperties(prefix = "mq.rabbit")
class RabbitMqProperties {
    @NotEmpty
    var host: String = ""

    var port: Int = 5672

    @NotEmpty
    var virtualHost: String = ""

    @NotEmpty
    var username: String = ""

    @NotEmpty
    var password: String = ""
}