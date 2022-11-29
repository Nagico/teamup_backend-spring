package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RecruitmentRepository : JpaRepository<Recruitment, Long>, JpaSpecificationExecutor<Recruitment> {
    fun findByTeam_RecruitingTrue(pageRequest: Pageable): Page<Recruitment>

    fun findByTeamId(teamId: Long, pageRequest: Pageable): Page<Recruitment>

}