package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
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
}