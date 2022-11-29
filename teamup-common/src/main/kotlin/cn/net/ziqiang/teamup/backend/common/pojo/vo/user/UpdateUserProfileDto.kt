package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "更新用户信息Dto")
data class UpdateUserProfileDto(
    @Schema(description = "真实姓名")
    var realName: String? = null,

    @Schema(description = "昵称")
    var username: String? = null,

    @Schema(description = "学院")
    var faculty: String? = null,

    @Schema(description = "年级")
    var grade: String? = null,

    @Schema(description = "学号")
    var studentId: String? = null,

    @Schema(description = "个人介绍")
    var introduction: String? = null,
)