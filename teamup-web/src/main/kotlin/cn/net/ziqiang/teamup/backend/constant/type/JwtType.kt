package cn.net.ziqiang.teamup.backend.constant.type


enum class JwtType(val string: String) {
    None(""),
    Auth("auth"),
    Refresh("refresh"),
}
