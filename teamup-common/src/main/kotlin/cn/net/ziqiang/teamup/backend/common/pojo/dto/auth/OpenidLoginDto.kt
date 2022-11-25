package cn.net.ziqiang.teamup.backend.common.pojo.dto.auth

import javax.validation.constraints.NotEmpty

data class OpenidLoginDto (
    @field:NotEmpty
    var openid: String = "",
)