package cn.net.ziqiang.teamup.backend.common.pojo.dto.auth

import javax.validation.constraints.NotEmpty

data class RefreshLoginDto(
    @field:NotEmpty
    var refresh: String = ""
)