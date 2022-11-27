package cn.net.ziqiang.teamup.backend.web.aop.log

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.pojo.entity.ExceptionLog
import cn.net.ziqiang.teamup.backend.common.pojo.entity.RequestLog
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.service.LogService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.concurrent.thread


@Aspect
@Component
@Slf4j
class LogAspect {
    @Autowired
    lateinit var logService: LogService

    //@Around("execution(* cn.net.ziqiang.teamup.backend.web.controller.*.*(..))")
    fun aroundController(joinPoint: ProceedingJoinPoint): Any? {
        logger.info("Controller: ${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}")
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val timeSt = System.currentTimeMillis()
        val result = joinPoint.proceed()
        val timeEd = System.currentTimeMillis()

        thread {
            val log = RequestLog()
            prepareRequestLog(log, joinPoint, request)
            log.time = (timeEd - timeSt) / 1000.0
            log.createTime = Date()
            logService.addRequestLog(log)
        }

        return result
    }

    //@AfterThrowing(pointcut = "execution(* cn.net.ziqiang.teamup.backend.web.controller.*.*(..))", throwing = "ex")
    fun logException(joinPoint: JoinPoint?, ex: Throwable, eid: String) {
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
            ?: return).request

        thread {
            val log = ExceptionLog()
            prepareRequestLog(log, joinPoint, request)
            log.eid = eid
            log.time = null
            log.createTime = Date()
            log.classType = ex::class.java.name
            if (ex is ApiException) {
                log.exceptionCode = ex.code
                log.exceptionType = ex.type.toString()
            }
            log.exceptionMsg = ex.message
            log.exceptionStack = ex.stackTraceToString()

            logService.addExceptionLog(log)
        }
    }

    private fun prepareRequestLog(
        log: RequestLog,
        joinPoint: JoinPoint?,
        request: HttpServletRequest
    ) {
        if (joinPoint != null)
            log.controllerMethod = "${joinPoint.signature.declaringTypeName}.${joinPoint.signature.name}"
        log.path = request.requestURI
        log.method = request.method
        log.ip = getIpAddr(request)

        val headerNames = request.headerNames
        val headers = mutableMapOf<String, String>()
        while (headerNames.hasMoreElements()) {
            val key = headerNames.nextElement()
            headers[key] = request.getHeader(key)
        }
        log.header = headers.toString()

        log.body = request.inputStream.bufferedReader().readText()
    }

    /**
     * 获取真实ip地址,不返回内网地址
     *
     * @param request
     * @return
     */
    fun getIpAddr(request: HttpServletRequest): String {
        //目前则是网关ip
        var ip = request.getHeader("X-Real-IP")
        if (ip != null && "" != ip && !"unknown".equals(ip, ignoreCase = true)) {
            return ip
        }
        ip = request.getHeader("X-Forwarded-For")
        return if (ip != null && "" != ip && !"unknown".equals(ip, ignoreCase = true)) {
            val index = ip.indexOf(',')
            if (index != -1) {
                //只获取第一个值
                ip.substring(0, index)
            } else {
                ip
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            request.remoteAddr
        }
    }

}