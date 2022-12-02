package cn.net.ziqiang.teamup.backend.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "wx.miniapp")
class WxMaProperties {
    var appid: String? = null
    var secret: String? = null
    var token: String? = null
    var aesKey: String? = null
    var msgDataFormat: String? = null
}