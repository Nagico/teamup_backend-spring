package cn.net.ziqiang.teamup.backend.common.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

@Schema(description = "更新用户信息Dto")
data class UpdateUserProfileDto(
    @field:NotEmpty
    @Schema(description = "真实姓名")
    var realName: String = "",

    @field:NotEmpty
    @Schema(description = "昵称")
    var nickname: String = "",

    @Pattern(regexp = "^1[3456789][0-9]{9}$")
    @Schema(description = "手机号")
    var phone: String = "",

    @Schema(description = "学院")
    var faculty: String = ""
)