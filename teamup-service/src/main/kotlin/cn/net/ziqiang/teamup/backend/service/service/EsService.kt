package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.es.TeamDoc

interface EsService {
    fun addTeamDoc(team: Team)

    fun updateTeamDoc(team: Team)

    fun deleteTeamDoc(teamId: Long)

    fun getTeamDocById(teamId: Long): TeamDoc

    fun getTeamDocListByCompetition(competition: String): List<TeamDoc>

    fun getTeamDocListByRole(role: String): List<TeamDoc>
}