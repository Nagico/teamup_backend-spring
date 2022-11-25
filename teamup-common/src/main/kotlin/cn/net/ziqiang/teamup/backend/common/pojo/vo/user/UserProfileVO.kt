package cn.net.ziqiang.teamup.backend.common.pojo.vo.user

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "用户资料VO")
data class UserProfileVO(
    @Schema(description = "用户ID")
    override var id: Long? = null,
    @Schema(description = "用户名")
    override var username: String = "",
    @Schema(description = "是否激活")
    var active: Boolean = false,
    @Schema(description = "真实姓名")
    var realName: String = "",
    @Schema(description = "学号")
    var studentId: String? = null,
    @Schema(description = "学院")
    override var faculty: String? = null,
    @Schema(description = "手机号")
    var phone: String? = null,
    @Schema(description = "年级")
    override var grade: String? = null,
    @Schema(description = "头像")
    override var avatar: String? = null,
    @Schema(description = "角色")
    var role: UserRole? = null,
) : UserInfo() {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        active = user.active,
        realName = user.realName,
        studentId = user.studentId,
        faculty = user.faculty,
        phone = user.phone,
        avatar = user.avatar,
        role = user.role,
        grade = user.grade
    )
}