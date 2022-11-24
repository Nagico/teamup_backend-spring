package cn.net.ziqiang.teamup.backend.common.bean.auth

import cn.net.ziqiang.teamup.backend.common.constant.type.JwtType
import cn.net.ziqiang.teamup.backend.common.constant.UserRole


data class JwtPayload(
    var userId: Long = -1,
    var role: UserRole = UserRole.None,
    var jwtType: JwtType = JwtType.Auth
)