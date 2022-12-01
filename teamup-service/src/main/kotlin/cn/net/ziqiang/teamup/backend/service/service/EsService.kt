package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team

interface EsService {
    fun addTeam(team: Team)

    fun updateTeam(team: Team)

    fun deleteTeam(teamId: Long)
}