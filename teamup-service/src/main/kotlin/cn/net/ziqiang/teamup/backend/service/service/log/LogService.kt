package cn.net.ziqiang.teamup.backend.service.service.log

import cn.net.ziqiang.teamup.backend.common.entity.ExceptionLog
import cn.net.ziqiang.teamup.backend.common.entity.RequestLog

interface LogService {
    fun addRequestLog(log: RequestLog)

    fun addExceptionLog(log: ExceptionLog)
}