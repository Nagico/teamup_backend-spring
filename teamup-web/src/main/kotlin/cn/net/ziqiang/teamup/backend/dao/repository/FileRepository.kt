package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface FileRepository : JpaRepository<cn.net.ziqiang.teamup.backend.pojo.entity.File, Long>, JpaSpecificationExecutor<cn.net.ziqiang.teamup.backend.pojo.entity.File> {
    fun findByExpiredFalseOrderByUser_CreateTimeAsc(): List<cn.net.ziqiang.teamup.backend.pojo.entity.File>

}