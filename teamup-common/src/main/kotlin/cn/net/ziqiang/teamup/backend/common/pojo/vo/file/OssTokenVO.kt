package cn.net.ziqiang.teamup.backend.common.pojo.vo.file

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "OSS直传参数VO")
data class OssTokenVO (
    @Schema(description = "access key id")
    var accessKeyId: String,
    @Schema(description = "上传文件object key")
    var key: String,
    @Schema(description = "上传url")
    var host: String,
    @Schema(description = "上传限制策略")
    var policy: String,
    @Schema(description = "签名")
    var signature: String,
    @Schema(description = "签名过期时间")
    var expire: Date,
    @Schema(description = "回调参数")
    var callback: String,
)