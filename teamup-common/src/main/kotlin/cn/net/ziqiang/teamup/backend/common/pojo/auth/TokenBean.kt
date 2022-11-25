package cn.net.ziqiang.teamup.backend.common.pojo.auth

import cn.net.ziqiang.teamup.backend.common.pojo.entity.User


data class TokenBean(
    var user: User? = null,
    var access: String = "",
    var refresh: String = "",
)