package cn.net.ziqiang.teamup.backend.common.pojo.es

import cn.net.ziqiang.teamup.backend.common.annotation.DocId
import cn.net.ziqiang.teamup.backend.common.annotation.EsClass
import cn.net.ziqiang.teamup.backend.common.annotation.EsField
import cn.net.ziqiang.teamup.backend.common.constant.type.EsDataType
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team

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
) {
    constructor(team: Team) : this(
        id = team.id,
        competitionField = team.competition!!.name,
        roleField = team.recruitmentRoles.map { it.name }.joinToString(","),
        recruiting = team.recruiting
    )
}