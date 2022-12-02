package cn.net.ziqiang.teamup.backend.common.util

import cn.net.ziqiang.teamup.backend.common.pojo.entity.User

fun User.getInfo() {
    realName = ""
    studentId = ""
    phone = ""
    introduction = ""
    openid = ""
}