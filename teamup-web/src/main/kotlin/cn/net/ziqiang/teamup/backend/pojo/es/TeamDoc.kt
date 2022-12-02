package cn.net.ziqiang.teamup.backend.pojo.es

import cn.net.ziqiang.teamup.backend.util.annotation.DocId
import cn.net.ziqiang.teamup.backend.util.annotation.EsClass
import cn.net.ziqiang.teamup.backend.util.annotation.EsField
import cn.net.ziqiang.teamup.backend.constant.type.EsDataType
import cn.net.ziqiang.teamup.backend.pojo.entity.Team

@EsClass
class TeamDoc (
    @DocId
    var id: Long? = null,

    @EsField(type = EsDataType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    var competitionField: String? = null,

    @EsField(type = EsDataType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    var roleField: String? = null,

    @EsField(type = EsDataType.BOOLEAN)
    var recruiting: Boolean? = null,

    @EsField(type = EsDataType.TEXT, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    var searchField: String? = null,
) {
    constructor(team: Team) : this(
        id = team.id,
        competitionField = team.competition!!.name,
        roleField = team.roles.map { it.name }.joinToString(" "),
        recruiting = team.recruiting,
        searchField = """
            ${team.name}
            ${team.competition!!.name}
            ${team.roles.map { it.name }.joinToString(" ")}
            ${team.tags.map { it.content }.joinToString(" ")}
            ${team.description}
            ${team.members.map { it.description }.joinToString(" ")}
        """.trimIndent()
    )
}