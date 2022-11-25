package cn.net.ziqiang.teamup.backend.service.vo.file

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "OSS回调VO")
data class OssCallbackVO (
    @Schema(description = "文件object key")
    var objectKey: String,
    @Schema(description = "文件大小")
    var size: Long,
)