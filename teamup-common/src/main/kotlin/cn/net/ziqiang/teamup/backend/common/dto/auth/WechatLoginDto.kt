package cn.net.ziqiang.teamup.backend.common.dto.auth

import javax.validation.constraints.NotEmpty

data class WechatLoginDto(
    @field:NotEmpty
    var encryptedData: String = "",

    @field:NotEmpty
    var iv: String = "",

    @field:NotEmpty
    var code: String = ""
)