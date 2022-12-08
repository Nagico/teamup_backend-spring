package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.vo.DateCountVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface TeamRepository : JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    fun findAllByCompetitionId(competitionId: Long, pageable: Pageable): Page<Team>
    fun findAllByLeaderId(creatorId: Long, pageable: Pageable): Page<Team>

    fun findAllByLeaderId(creatorId: Long): List<Team>

    fun findAllByIdIn(ids: List<Long>, pageable: Pageable): Page<Team>
    fun findAllByIdIn(ids: Set<Long>): Set<Team>

    fun countByLeaderId(creatorId: Long): Long

    @Query(value = "SELECT " +
            "new cn.net.ziqiang.teamup.backend.pojo.vo.DateCountVO(" +
                "function('date_format', t.createTime, '%Y-%m-%d')," +
                "count(t)" +
             ") " +
            "FROM Team t " +
            "WHERE t.competition.id = :id " +
            "GROUP BY function('date_format', t.createTime, '%Y-%m-%d')")
    fun countGroupByCreateDateById(id: Long): List<DateCountVO>
}