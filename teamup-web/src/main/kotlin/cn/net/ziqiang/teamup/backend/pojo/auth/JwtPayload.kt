package cn.net.ziqiang.teamup.backend.pojo.auth

import cn.net.ziqiang.teamup.backend.constant.type.JwtType
import cn.net.ziqiang.teamup.backend.constant.UserRole


data class JwtPayload(
    var userId: Long = -1,
    var role: UserRole = UserRole.None,
    var username: String = "",
    var jwtType: JwtType = JwtType.Auth
)