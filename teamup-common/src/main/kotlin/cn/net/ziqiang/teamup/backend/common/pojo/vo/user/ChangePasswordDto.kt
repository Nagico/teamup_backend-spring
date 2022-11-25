package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import javax.validation.constraints.NotEmpty

class ChangePasswordDto {
    @field:NotEmpty
    var oldPassword: String = ""

    @field:NotEmpty
    val newPassword: String = ""
}