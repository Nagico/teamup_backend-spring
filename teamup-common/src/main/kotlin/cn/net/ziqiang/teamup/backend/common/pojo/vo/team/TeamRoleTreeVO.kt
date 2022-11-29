package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

import cn.net.ziqiang.teamup.backend.common.pojo.entity.TeamRole

data class TeamRoleTreeVO(
    var id: Long? = null,
    var name: String? = null,
    var description: String? = null,
    var level: Int? = null,

    var children: MutableList<TeamRoleTreeVO>? = null,
) {
    constructor(teamRole: TeamRole) : this(
        id = teamRole.id,
        name = teamRole.name,
        description = teamRole.description,
        level = teamRole.level,
    )
}
