package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

data class TeamMemberDto(
    var roles: List<Long>? = null,

    var faculty: String? = null,

    var description: String? = null,
)