package cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team

data class RecruitmentDto(
    var team: Team? = null,

    var role: Long? = null,

    var requirements: List<String>? = null,
)
