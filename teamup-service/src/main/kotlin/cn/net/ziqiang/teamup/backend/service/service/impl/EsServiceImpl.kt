package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.es.TeamDoc
import cn.net.ziqiang.teamup.backend.service.business.EsBusiness
import cn.net.ziqiang.teamup.backend.service.service.EsService
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class EsServiceImpl(@Autowired private val esBusiness: EsBusiness) : EsService {
    init {
        esBusiness.createIndexSettingsMappings(TeamDoc::class.java)
    }


    override fun addTeamDoc(team: Team) {
        esBusiness.addData(TeamDoc(team), true)
    }

    override fun updateTeamDoc(team: Team) {
        esBusiness.updateByDocId(team.id.toString(), TeamDoc(team), true)
    }

    override fun deleteTeamDoc(teamId: Long) {
        esBusiness.deleteByDocId(teamId.toString(), TeamDoc::class.java)
    }

    override fun getTeamDocById(teamId: Long): TeamDoc {
        return esBusiness.getByDocId(teamId.toString(), TeamDoc::class.java)
    }

    override fun getTeamDocListByCompetition(competition: String): List<TeamDoc> {
        val query = Query.of{
            q -> q.match {
                m->m.field("competitionField").query(competition)
            }
        }
        val res = esBusiness.complexQuery(query, TeamDoc::class.java)
        return (res as MutableList<TeamDoc?>).apply {
            removeAll { it == null }
        }.map { it!! }
    }

    override fun getTeamDocListByRole(role: String): List<TeamDoc> {
        val query = Query.of {
                q -> q.match {
                m->m.field("roleField").query(role)
            }
        }
        val res = esBusiness.complexQuery(query, TeamDoc::class.java)
        return (res as MutableList<TeamDoc?>).apply {
            removeAll { it == null || it.recruiting == false }
        }.map { it!! }
    }
}