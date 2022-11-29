package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

import cn.net.ziqiang.teamup.backend.common.pojo.entity.TeamRole

data class TeamRoleVO(
    var id: Long? = null,
    var name: String? = null,
) {
    constructor(teamRole: TeamRole) : this(
        id = teamRole.id,
        name = teamRole.name,
    )
}
