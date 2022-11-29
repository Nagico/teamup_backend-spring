package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

import cn.net.ziqiang.teamup.backend.common.pojo.entity.TeamMember

class TeamDto (
    var name: String? = null,

    var competition: Long? = null,

    var description: String? = null,

    var tags: List<String>? = null,

    var members: List<TeamMember>? = null,

    var recruiting: Boolean? = null,
)