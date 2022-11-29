package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamInfoVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamVO
import org.springframework.data.domain.PageRequest

interface TeamService {
    fun getUserTeams(userId: Long, pageRequest: PageRequest): PagedList<Team, TeamInfoVO>

    fun getTeamDetail(teamId: Long): TeamVO

    fun createTeam(userId: Long, dto: TeamDto): TeamVO

    fun updateTeam(teamId: Long, dto: TeamDto): TeamVO

    fun deleteTeam(teamId: Long)

    fun getTeamRecruitments(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO>

    fun createTeamRecruitment(teamId: Long, dto: RecruitmentDto): RecruitmentVO

    fun updateTeamRecruitment(teamId: Long, recruitmentId: Long, dto: RecruitmentDto): RecruitmentVO

    fun deleteTeamRecruitment(teamId: Long, recruitmentId: Long)
}