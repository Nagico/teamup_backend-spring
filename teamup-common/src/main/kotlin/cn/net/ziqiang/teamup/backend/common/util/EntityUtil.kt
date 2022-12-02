package cn.net.ziqiang.teamup.backend.common.util

import cn.net.ziqiang.teamup.backend.common.pojo.entity.User

fun User.getInfo(): User {
    return User(
        id = this.id,
        username = this.username,
        avatar = this.avatar,
        faculty = this.faculty,
        grade = this.grade,
        lastLogin = this.lastLogin,
        status = this.status
    )
}