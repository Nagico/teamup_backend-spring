package cn.net.ziqiang.teamup.backend.common.constant.type


enum class JwtType(val string: String) {
    None(""),
    Auth("auth"),
    Refresh("refresh"),
}
