package cn.net.ziqiang.teamup.backend.util.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Component
@Validated
@ConfigurationProperties(prefix = "aliyun.sms")
class SmsProperties (
    @NotEmpty
    var accessKeyId: String = "",
    @NotEmpty
    var accessKeySecret: String = "",
    @NotEmpty
    var signName: String = "",

    var templateCode: List<TemplateCode> = listOf()

) {
    data class TemplateCode(
        @NotEmpty
        var name: String = "",
        @NotEmpty
        var code: String = "",
    )

    fun getTemplateCodeByName(name: String): String {
        return templateCode.first { it.name == name }.code
    }
}