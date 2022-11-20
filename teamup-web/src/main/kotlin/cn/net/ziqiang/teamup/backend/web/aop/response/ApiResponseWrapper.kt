package cn.net.ziqiang.teamup.backend.web.aop.response

import cn.net.ziqiang.teamup.backend.service.vo.ResultVO
import com.alibaba.fastjson.JSON
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * API响应包装器
 *
 */
@RestControllerAdvice
class ApiResponseWrapper : ResponseBodyAdvice<Any> {
    /**
     * 支持包装
     */
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return !returnType.declaringClass.name.contains("OpenApi")
    }

    /**
     * 包装响应
     *
     */
    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any {
        if (body is String){
            // 输出 JSON 头信息
            response.headers.set("Content-Type", "application/json;charset=UTF-8")
            return JSON.toJSONString(ResultVO.success(body))
        }
        // 处理全局异常处理后返回的结果
        if (body is ResultVO<*>) {
            response.headers.set("Content-Type", "application/json;charset=UTF-8")
            response.setStatusCode(body.httpStatus)
            return body
        }

        return ResultVO.success(body)
    }

}