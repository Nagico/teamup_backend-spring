package cn.net.ziqiang.teamup.backend.web.aop.response

import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class Response404Handler(serverProperties: ServerProperties): BasicErrorController(DefaultErrorAttributes(), serverProperties.error) {
    /**
     * 覆盖默认的JSON响应
     */
    override fun error(request: HttpServletRequest): Nothing {
        throw ApiException(ResultType.APINotFound)
    }
}