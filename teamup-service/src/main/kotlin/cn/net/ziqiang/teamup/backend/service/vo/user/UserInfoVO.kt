package cn.net.ziqiang.teamup.backend.service.vo.user

import cn.net.ziqiang.teamup.backend.common.entity.User

data class UserInfoVO(
    override var id: Long?,
    override var username: String,
    override var faculty: String?,
    override var avatar: String?
) : UserInfo() {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        faculty = user.faculty,
        avatar = user.avatar)
}