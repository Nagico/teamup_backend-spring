package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import javax.validation.constraints.NotEmpty

class ResetPasswordDto {
    @field:NotEmpty
    val phone: String = ""

    @field:NotEmpty
    val verifyCode: String = ""

    @field:NotEmpty
    val password: String = ""
}