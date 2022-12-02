package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TeamRepository : JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    fun findAllByCompetitionId(competitionId: Long, pageable: Pageable): Page<Team>
    fun findAllByLeaderId(creatorId: Long, pageable: Pageable): Page<Team>

    fun findAllByLeaderId(creatorId: Long): List<Team>

    fun findAllByIdIn(ids: List<Long>, pageable: Pageable): Page<Team>
}