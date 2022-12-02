package cn.net.ziqiang.teamup.backend.common.pojo.vo.team

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Tag
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.vo.user.UserInfoVO

data class TeamInfoVO(
    var id: Long,

    var name: String,

    var competition: Competition,

    var description: String,

    var leader: UserInfoVO,

    var likeCount: Long,

    var tags: List<Tag>,

    var roles: Set<TeamRoleVO>,
) {
    constructor(team: Team) : this(
        team.id!!,
        team.name!!,
        team.competition!!,
        team.description!!,
        UserInfoVO(team.leader!!),
        team.likeCount,
        team.tags,
        team.roles
    )
}
