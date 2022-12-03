package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.ExceptionLog
import cn.net.ziqiang.teamup.backend.pojo.entity.RequestLog

interface LogService {
    fun addRequestLog(log: RequestLog)

    fun addExceptionLog(log: ExceptionLog)
}