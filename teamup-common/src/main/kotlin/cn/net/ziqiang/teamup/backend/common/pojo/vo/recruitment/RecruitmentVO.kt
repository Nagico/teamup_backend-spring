package cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamRoleVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamInfoVO

data class RecruitmentVO(
    var id: Long,
    var team: TeamInfoVO,
    var role: TeamRoleVO,
    var requirements: List<String>,
){
    constructor(recruitment: Recruitment) : this(
        recruitment.id!!,
        TeamInfoVO(recruitment.team!!),
        TeamRoleVO(recruitment.role!!),
        recruitment.requirements
    )
}
