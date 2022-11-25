package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.common.entity.RequestLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RequestLogRepository : JpaRepository<RequestLog, Long>, JpaSpecificationExecutor<RequestLog> {

}