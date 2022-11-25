package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

class RegisterDto {
    @Pattern(regexp = "^1[3456789][0-9]{9}$")
    @field:NotEmpty
    val phone: String = ""

    @field:NotEmpty
    val verifyCode: String = ""

    @field:NotEmpty
    var password: String = ""
}