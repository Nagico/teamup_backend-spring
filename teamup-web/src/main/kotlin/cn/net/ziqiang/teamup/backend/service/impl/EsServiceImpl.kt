package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.es.TeamDoc
import cn.net.ziqiang.teamup.backend.business.EsBusiness
import cn.net.ziqiang.teamup.backend.service.EsService
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
        if (esBusiness.checkDocId(team.id.toString(), TeamDoc::class.java)) {
            esBusiness.deleteByDocId(team.id.toString(), TeamDoc::class.java)
        }
        esBusiness.addData(TeamDoc(team), true)
    }

    override fun deleteTeamDoc(teamId: Long) {
        if (esBusiness.checkDocId(teamId.toString(), TeamDoc::class.java)) {
            esBusiness.deleteByDocId(teamId.toString(), TeamDoc::class.java)
        }
    }

    override fun getAllTeamDocs(): List<TeamDoc> {
        return (esBusiness.queryAll(TeamDoc::class.java) as MutableList<TeamDoc?>).apply {
            this.removeIf { it == null }
        }.map { it!! }
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

    override fun getTeamDocListBySearch(search: String): List<TeamDoc> {
        val query = Query.of {
                q -> q.match {
                m->m.field("searchField").query(search)
            }
        }
        val res = esBusiness.complexQuery(query, TeamDoc::class.java)
        return (res as MutableList<TeamDoc?>).apply {
            removeAll { it == null }
        }.map { it!! }
    }

    override fun rebuildIndex(items: List<TeamDoc>) {
        esBusiness.deleteByQuery(Query.of { q -> q.matchAll { m -> m } }, TeamDoc::class.java)
        esBusiness.addDataList(items, true)
    }
}