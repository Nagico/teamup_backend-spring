package cn.net.ziqiang.teamup.backend.service.vo

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

/**
 * API响应类
 *
 * @param T 响应数据类型
 * @property code 响应码
 * @property message 响应消息
 * @property data 响应内容
 */
@Schema(description = "响应VO")
data class ResultVO<T>(
    @Schema(description = "响应代码")
    var code: String,
    @Schema(description = "反馈消息")
    var message: String,
    @Schema(description = "数据")
    var data: T?,
    @JsonIgnore var httpStatus: HttpStatus,
) {
    constructor(type: ResultType, message: String = "", data: T? = null):
            this(code = type.code, message = message.ifEmpty { type.message }, data = data, httpStatus = type.httpStatus)

    companion object {
        fun <T> success(message: String, data: T): ResultVO<T> {
            return ResultVO(ResultType.Success.code, message, data, ResultType.Success.httpStatus)
        }

        fun <T> success(data: T): ResultVO<T> {
            return ResultVO(ResultType.Success.code, ResultType.Success.message, data, ResultType.Success.httpStatus)
        }

        fun <T> fail(code: String, message: String, httpStatus: HttpStatus): ResultVO<T> {
            return ResultVO(code, message, null, httpStatus)
        }

        fun <T> fail(type: ResultType, message: String? = null): ResultVO<T> {
            return ResultVO(type.code, message?:type.message, null, type.httpStatus)
        }
    }
}