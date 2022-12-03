package cn.net.ziqiang.teamup.backend.util.aop.exception

import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.vo.ResultVO
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.util.aop.log.LogAspect
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import io.sentry.Sentry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@Slf4j
@RestControllerAdvice
@ControllerAdvice
class ApiExceptionHandler {
    @Autowired
    private lateinit var logAspect: LogAspect

    /**
     * 处理自定义异常
     *
     * @param e 自定义异常
     * @return 标准API响应
     */
    @ResponseBody
    @ExceptionHandler(ApiException::class)
    @MessageExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResultVO<String> {
        logger.error(e.message)

        if (e.type.record) {
            logger.info(e.stackTraceToString())
            val eid = notifySentry(e)
            logAspect.logException(null, e, eid)
        }

        return ResultVO.fail(e.code, e.message, e.httpStatus)
    }

    /**
     * 处理未知异常
     *
     * @param e 未知异常
     * @return 标准API响应
     */
    @ResponseBody
    @ExceptionHandler(Throwable::class)
    @MessageExceptionHandler(Throwable::class)
    fun handleException(e: Throwable): ResultVO<String> {
        logger.error(e.message)

        if (e is org.springframework.security.access.AccessDeniedException) {
            return ResultVO.fail(ResultType.PermissionDenied)
        }

        if (e is org.springframework.web.servlet.NoHandlerFoundException) {
            return ResultVO.fail(ResultType.APINotFound)
        }

        if (e is org.springframework.web.HttpRequestMethodNotSupportedException) {
            return ResultVO.fail(ResultType.MethodNotAllowed)
        }

        if (e is org.springframework.web.bind.MethodArgumentNotValidException) {
            return ResultVO.fail(ResultType.ParamValidationFailed)
        }

        if (e is org.springframework.web.bind.MissingServletRequestParameterException) {
            return ResultVO.fail(ResultType.ParamValidationFailed)
        }

        if (e is org.springframework.web.multipart.MaxUploadSizeExceededException) {
            return ResultVO.fail(ResultType.ParamValidationFailed, "文件大小超过5M限制")
        }

        if (e is org.springframework.web.multipart.MultipartException) {
            return ResultVO.fail(ResultType.HeaderNotAcceptable, "仅支持multipart/form-data格式")
        }

        if (e is org.springframework.web.HttpMediaTypeNotSupportedException) {
            return ResultVO.fail(ResultType.HeaderNotAcceptable, "仅支持application/json格式")
        }

        if (e is org.springframework.http.converter.HttpMessageNotReadableException) {
            return ResultVO.fail(ResultType.JSONParseFailed, "JSON解析失败")
        }

        logger.info(e.stackTraceToString())
        val eid = notifySentry(e)
        logAspect.logException(null, e, eid)

        return ResultVO(ResultType.ServerError.code, ResultType.ServerError.message, e.message, ResultType.ServerError.httpStatus)
    }


    // region Utils
    fun notifySentry(e: Throwable) : String {
        val user = SecurityContextUtils.userOrNull
        if (user == null) {
            Sentry.configureScope { scope -> scope.setTag("role", "Anonymous") }
        } else {
            Sentry.configureScope { scope -> scope.setTag("role", user.role.toString()) }
        }

        if (e is ApiException) {
            Sentry.configureScope { scope ->
                run {
                    scope.setExtra("code", e.code)
                    scope.setExtra("type", e.type.toString())
                    scope.setExtra("message", e.message)
                }
            }
        }

        return Sentry.captureException(e).toString()
    }

    // endregion
}