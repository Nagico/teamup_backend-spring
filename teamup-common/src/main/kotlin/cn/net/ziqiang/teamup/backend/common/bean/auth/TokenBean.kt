package cn.net.ziqiang.teamup.backend.common.bean.auth


data class TokenBean(
    var auth: String = "",
    var refresh: String = "",
)