package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.TeamRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TeamRoleRepository : JpaRepository<TeamRole, Long>, JpaSpecificationExecutor<TeamRole> {
    fun getAllByIdIn(ids: List<Long>): List<TeamRole>
    fun getAllByIdIn(ids: Set<Long>): List<TeamRole>
}