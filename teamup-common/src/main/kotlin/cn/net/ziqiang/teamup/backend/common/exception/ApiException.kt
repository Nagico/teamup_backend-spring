package cn.net.ziqiang.teamup.backend.common.exception

import cn.net.ziqiang.teamup.backend.common.response.ApiResponseType
import org.springframework.http.HttpStatus

/**
 * API异常类
 *
 */
class ApiException : Exception {
    var type: ApiResponseType
    var code: String
    var httpStatus: HttpStatus
    override var message: String

    constructor(type: ApiResponseType = ApiResponseType.ServerError) : super(type.message) {
        this.type = type
        this.code = type.code
        this.httpStatus = type.httpStatus
        this.message = type.message
    }
    constructor(type: ApiResponseType, cause: Throwable) : super(cause) {
        this.type = type
        this.code = type.code
        this.httpStatus = type.httpStatus
        this.message = type.message
    }
    constructor(type: ApiResponseType, message: String) : super(message) {
        this.type = type
        this.code = type.code
        this.httpStatus = type.httpStatus
        this.message = message
    }
    constructor(type: ApiResponseType, message: String, cause: Throwable) : super(message, cause) {
        this.type = type
        this.code = type.code
        this.httpStatus = type.httpStatus
        this.message = message
    }
}