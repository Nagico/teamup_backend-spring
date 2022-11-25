package cn.net.ziqiang.teamup.backend.common.pojo.vo.auth

import cn.net.ziqiang.teamup.backend.common.pojo.auth.TokenBean
import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "认证VO")
data class AuthVO (
    @Schema(description = "用户id")
    var id: Long = -1,

    @Schema(description = "用户名")
    var username: String = "",

    @Schema(description = "激活")
    var active: Boolean = false,

    @Schema(description = "用户角色")
    var role: UserRole = UserRole.None,

    @Schema(description = "认证Token")
    var access: String = "",

    @Schema(description = "刷新Token")
    var refresh: String = ""
) {
    constructor(token: TokenBean) : this(
        id = token.user!!.id!!,
        username = token.user!!.username,
        active = token.user!!.active,
        role = token.user!!.role,
        access = token.access,
        refresh = token.refresh
    )
}