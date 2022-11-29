package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

class TeamDto (
    var name: String? = null,

    var competition: Long? = null,

    var description: String? = null,

    var tags: List<String>? = null,

    var members: List<TeamMemberDto>? = null,

    var recruiting: Boolean? = null,
)