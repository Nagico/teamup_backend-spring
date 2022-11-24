package cn.net.ziqiang.teamup.backend.service.vo.user

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.entity.User

data class UserProfileVO(
    override var id: Long? = null,
    override var username: String = "",
    var active: Boolean = false,
    var realName: String = "",
    var studentId: String? = null,
    override var faculty: String? = null,
    var phone: String? = null,
    override var avatar: String? = null,
    var role: UserRole? = null,
) : UserInfo() {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        active = user.active,
        realName = user.realName,
        studentId = user.studentId,
        faculty = user.faculty,
        phone = user.phone,
        avatar = user.avatar,
        role = user.role
    )
}