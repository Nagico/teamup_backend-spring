package cn.net.ziqiang.teamup.backend.pojo.vo.user


data class UserDto (
    var oldPassword: String = "",

    var password: String = "",

    val phone: String = "",

    val verifyCode: String = "",
)