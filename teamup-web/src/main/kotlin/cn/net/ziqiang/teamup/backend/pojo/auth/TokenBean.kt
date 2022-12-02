package cn.net.ziqiang.teamup.backend.pojo.auth

import cn.net.ziqiang.teamup.backend.pojo.entity.User


data class TokenBean(
    var user: User? = null,
    var access: String = "",
    var refresh: String = "",
)