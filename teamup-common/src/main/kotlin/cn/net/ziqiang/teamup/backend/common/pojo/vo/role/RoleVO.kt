package cn.net.ziqiang.teamup.backend.common.pojo.vo.role

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Role

data class RoleVO(
    var id: Long? = null,
    var name: String? = null,
) {
    constructor(role: Role) : this(
        id = role.id,
        name = role.name,
    )
}
