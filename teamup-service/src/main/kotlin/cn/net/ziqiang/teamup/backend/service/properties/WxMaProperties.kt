package cn.net.ziqiang.teamup.backend.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "wx.miniapp")
class WxMaProperties {
    private lateinit var configs: List<Config>

    data class Config(
        var appid: String? = null,
        var secret: String? = null,
        var token: String? = null,
        var aesKey: String? = null,
        var msgDataFormat: String? = null
    )
}