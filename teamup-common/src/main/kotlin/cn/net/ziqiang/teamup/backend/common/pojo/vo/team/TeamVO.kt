package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

import cn.net.ziqiang.teamup.backend.common.pojo.entity.*
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.UserInfoVO

data class TeamVO (
    var id: Long,

    var name: String,

    var competition: Competition,

    var description: String,

    var leader: UserInfoVO,

    var likeCount: Long,

    var members: List<TeamMember>,

    var tags: List<Tag>,

    var roles: Set<TeamRoleVO>,

    var recruiting: Boolean,
) {
    constructor(team: Team) : this(
        team.id!!,
        team.name!!,
        team.competition!!,
        team.description!!,
        UserInfoVO(team.leader!!),
        team.likeCount,
        team.members,
        team.tags,
        team.recruitmentRoles,
        team.recruiting,
    )
}