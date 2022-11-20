package cn.net.ziqiang.teamup.backend.common.bean.auth

import cn.net.ziqiang.teamup.backend.common.entity.User


data class TokenBean(
    var user: User? = null,
    var access: String = "",
    var refresh: String = "",
)