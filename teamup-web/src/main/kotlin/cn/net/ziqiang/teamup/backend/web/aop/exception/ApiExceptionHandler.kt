package cn.net.ziqiang.teamup.backend.web.aop.exception

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.web.vo.ResultVO
import cn.net.ziqiang.teamup.backend.common.constant.ResultType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@Slf4j
@RestControllerAdvice
class ApiExceptionHandler {
    /**
     * 处理自定义异常
     *
     * @param e 自定义异常
     * @return 标准API响应
     */
    @ResponseBody
    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResultVO<String> {
        logger.error(e.message)
        return ResultVO.fail(e.code, e.message, e.httpStatus)
    }

    /**
     * 处理未知异常
     *
     * @param e 未知异常
     * @return 标准API响应
     */
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResultVO<String> {
        logger.error(e.message)
        if (logger.isInfoEnabled) {
            e.printStackTrace()
        }

        if (e is org.springframework.security.access.AccessDeniedException) {
            return ResultVO.fail(ResultType.PermissionDenied)
        }

        return ResultVO(ResultType.ServerError.code, ResultType.ServerError.message, e.message, ResultType.ServerError.httpStatus)
    }
}