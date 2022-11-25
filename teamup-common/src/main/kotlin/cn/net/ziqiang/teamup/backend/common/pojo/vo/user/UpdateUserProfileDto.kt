package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "更新用户信息Dto")
data class UpdateUserProfileDto(
    @Schema(description = "真实姓名")
    var realName: String = "",

    @Schema(description = "昵称")
    var username: String = "",

    @Schema(description = "学院")
    var faculty: String = "",

    @Schema(description = "年级")
    var grade: String = "",
)