package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.pojo.entity.ExceptionLog
import cn.net.ziqiang.teamup.backend.pojo.entity.RequestLog
import cn.net.ziqiang.teamup.backend.dao.repository.ExceptionLogRepository
import cn.net.ziqiang.teamup.backend.dao.repository.RequestLogRepository
import cn.net.ziqiang.teamup.backend.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LogServiceImpl : LogService {
    @Autowired
    private lateinit var requestLogRepository: RequestLogRepository

    @Autowired
    private lateinit var exceptionLogRepository: ExceptionLogRepository

    override fun addRequestLog(log: RequestLog) {
        requestLogRepository.save(log)
    }

    override fun addExceptionLog(log: ExceptionLog) {
        exceptionLogRepository.save(log)
    }
}