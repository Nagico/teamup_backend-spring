package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import cn.net.ziqiang.teamup.backend.common.constant.status.UserStatus
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "用户简要信息VO")
data class UserInfoVO(
    @Schema(description = "用户ID")
    var id: Long?,
    @Schema(description = "用户名")
    var username: String,
    @Schema(description = "学院")
    var faculty: String?,
    @Schema(description = "年级")
    var grade: String? = null,
    @Schema(description = "头像")
    var avatar: String?,

    @Schema(description = "最近出现时间")
    var lastLogin: Date? = null,
    @Schema(description = "当前状态")
    var status: UserStatus? = null
    ) {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        faculty = user.faculty,
        grade = user.grade,
        avatar = user.avatar,
        lastLogin = user.lastLogin
    )
}