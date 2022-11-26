package cn.net.ziqiang.teamup.backend.web.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "host.cors")
class CorsProperties {
    var whitelists: List<String> = listOf("*")
}