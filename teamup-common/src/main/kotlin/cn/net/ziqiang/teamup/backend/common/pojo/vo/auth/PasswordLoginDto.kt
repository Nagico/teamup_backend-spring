package cn.net.ziqiang.teamup.backend.common.pojo.vo.auth

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

class PasswordLoginDto {
    @field:NotEmpty
    @Pattern(regexp = "^1[3456789][0-9]{9}$")
    var phone: String = ""
    @field:NotEmpty
    var password: String = ""
}