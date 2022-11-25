package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.entity.ExceptionLog
import cn.net.ziqiang.teamup.backend.common.pojo.entity.RequestLog

interface LogService {
    fun addRequestLog(log: RequestLog)

    fun addExceptionLog(log: ExceptionLog)
}