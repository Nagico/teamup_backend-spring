package cn.net.ziqiang.teamup.backend.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Component
@Validated
@ConfigurationProperties(prefix = "security.jwt")
class JwtProperties {
    @NotEmpty(message = "JwtSecret不能为空")
    var secret: String = ""
}