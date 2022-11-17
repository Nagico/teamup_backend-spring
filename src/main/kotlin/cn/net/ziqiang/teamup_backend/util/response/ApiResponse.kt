package cn.net.ziqiang.teamup_backend.util.response

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus

/**
 * API响应类
 *
 * @param T 响应数据类型
 * @property code 响应码
 * @property message 响应消息
 * @property data 响应内容
 */
data class ApiResponse<T>(
    var code: String,
    var message: String,
    var data: T?,
    @JsonIgnore var httpStatus: HttpStatus,
) {
    companion object {
        fun <T> success(message: String, data: T): ApiResponse<T> {
            return ApiResponse(ApiResponseType.Success.code, message, data, ApiResponseType.Success.httpStatus)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(ApiResponseType.Success.code, ApiResponseType.Success.message, data, ApiResponseType.Success.httpStatus)
        }

        fun <T> fail(code: String, message: String, httpStatus: HttpStatus): ApiResponse<T> {
            return ApiResponse(code, message, null, httpStatus)
        }

        fun <T> fail(type: ApiResponseType): ApiResponse<T> {
            return ApiResponse(type.code, type.message, null, type.httpStatus)
        }
    }
}