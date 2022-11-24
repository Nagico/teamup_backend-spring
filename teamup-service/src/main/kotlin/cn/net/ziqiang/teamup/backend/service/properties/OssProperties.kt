package cn.net.ziqiang.teamup.backend.service.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Component
@Validated
@ConfigurationProperties(prefix = "aliyun.oss")
class OssProperties (
    @NotEmpty
    var accessKeyId: String = "",
    @NotEmpty
    var accessKeySecret: String = "",
    @NotEmpty
    var endpoint: String = "",
    @NotEmpty
    var bucketName: String = "",
)