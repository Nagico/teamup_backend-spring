package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.ExceptionLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ExceptionLogRepository : JpaRepository<ExceptionLog, Long>, JpaSpecificationExecutor<ExceptionLog> {

}