package cn.net.ziqiang.teamup.backend.service.vo.file

import java.util.*

data class OssTokenVO (
    var accessKeyId: String,
    var host: String,
    var policy: String,
    var signature: String,
    var expire: Date,
    var callback: String,
)