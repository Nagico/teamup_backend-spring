package cn.net.ziqiang.teamup.backend.service.vo.user

import cn.net.ziqiang.teamup.backend.common.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "用户简要信息VO")
data class UserInfoVO(
    @Schema(description = "用户ID")
    override var id: Long?,
    @Schema(description = "用户名")
    override var username: String,
    @Schema(description = "学院")
    override var faculty: String?,
    @Schema(description = "年级")
    override var grade: String? = null,
    @Schema(description = "头像")
    override var avatar: String?
) : UserInfo() {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        faculty = user.faculty,
        grade = user.grade,
        avatar = user.avatar
    )
}