package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.es.TeamDoc
import cn.net.ziqiang.teamup.backend.service.business.EsBusiness
import cn.net.ziqiang.teamup.backend.service.service.EsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class EsServiceImpl(@Autowired private val esBusiness: EsBusiness) : EsService {
    init {
        esBusiness.createIndexSettingsMappings(TeamDoc::class.java)
    }


    override fun addTeam(team: Team) {
        esBusiness.addData(team, true)
    }

    override fun updateTeam(team: Team) {
        esBusiness.updateByDocId(team.id.toString(), team, true)
    }

    override fun deleteTeam(teamId: Long) {
        esBusiness.deleteByDocId(teamId.toString(), TeamDoc::class.java)
    }
}