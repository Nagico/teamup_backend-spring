package cn.net.ziqiang.teamup_backend.util.response

import org.springframework.http.HttpStatus

/**
 * API响应类型
 *
 * @property code 响应码
 * @property message 响应消息
 * @property httpStatus HTTP状态码
 */
enum class ApiResponseType(var code: String, var message: String, var httpStatus: HttpStatus) {
    Success("00000", "", HttpStatus.OK),
    ClientError("A0000", "用户端错误", HttpStatus.BAD_REQUEST),
    LoginError("A0200", "用户登陆异常", HttpStatus.BAD_REQUEST),
    LoginFailed("A0210", "用户登录失败", HttpStatus.BAD_REQUEST),
    UsernameNotExist("A0211", "用户名不存在", HttpStatus.BAD_REQUEST),
    PasswordWrong("A0212", "用户密码错误", HttpStatus.BAD_REQUEST),
    LoginFailedExceed("A0213", "用户输入密码次数超限", HttpStatus.BAD_REQUEST),
    LoginExpired("A0220", "用户登录已过期", HttpStatus.UNAUTHORIZED),
    TokenInvalid("A0221", "token 无效或已过期", HttpStatus.UNAUTHORIZED),
    ThirdLoginFailed("A0230", "用户第三方登录失败", HttpStatus.UNAUTHORIZED),
    ThirdLoginCaptchaError("A0232", "用户第三方登录验证码错误", HttpStatus.UNAUTHORIZED),
    ThirdLoginExpired("A0233", "用户第三方登录已过期", HttpStatus.UNAUTHORIZED),
    PermissionError("A0300", "用户权限异常", HttpStatus.FORBIDDEN),
    NotLogin("A0310", "用户未登录", HttpStatus.UNAUTHORIZED),
    NotActive("A0311", "用户未激活", HttpStatus.FORBIDDEN),
    PermissionDenied("A0312", "用户无权限", HttpStatus.FORBIDDEN),
    ServiceNotAvailable("A0313", "不在服务时段", HttpStatus.FORBIDDEN),
    UserBlocked("A0320", "黑名单用户", HttpStatus.FORBIDDEN),
    UserFrozen("A0321", "账号被冻结", HttpStatus.FORBIDDEN),
    IPInvalid("A0322", "非法 IP 地址", HttpStatus.UNAUTHORIZED),
    ParamError("A0HttpStatus.BAD_REQUEST", "用户请求参数错误", HttpStatus.BAD_REQUEST),
    JSONParseFailed("A0410", "请求 JSON 解析错误", HttpStatus.BAD_REQUEST),
    ParamEmpty("A0420", "请求必填参数为空", HttpStatus.BAD_REQUEST),
    ParamValidationFailed("A0430", "请求参数值校验失败", HttpStatus.BAD_REQUEST),
    RequestError("A0500", "用户请求服务异常", HttpStatus.BAD_REQUEST),
    APINotFound("A0510", "请求接口不存在", HttpStatus.NOT_FOUND),
    MethodNotAllowed("A0511", "请求方法不允许", HttpStatus.METHOD_NOT_ALLOWED),
    APIThrottled("A0512", "请求次数超出限制", HttpStatus.TOO_MANY_REQUESTS),
    HeaderNotAcceptable("A0513", "请求头无法满足", HttpStatus.NOT_ACCEPTABLE),
    ResourceNotFound("A0514", "请求资源不存在", HttpStatus.NOT_FOUND),
    UploadError("A0600", "用户上传文件异常", HttpStatus.BAD_REQUEST),
    UnsupportedMediaType("A0610", "用户上传文件类型不支持", HttpStatus.BAD_REQUEST),
    UnsupportedMediaSize("A0613", "用户上传文件大小错误", HttpStatus.BAD_REQUEST),
    VersionError("A0700", "用户版本异常", HttpStatus.BAD_REQUEST),
    AppVersionError("A0710", "用户应用安装版本不匹配", HttpStatus.BAD_REQUEST),
    APIVersionError("A0720", "用户 API 请求版本不匹配", HttpStatus.BAD_REQUEST),
    ServerError("B0000", "系统执行出错", HttpStatus.INTERNAL_SERVER_ERROR),
    ServerTimeout("B0100", "系统执行超时", HttpStatus.INTERNAL_SERVER_ERROR),
    ServerResourceError("B0200", "系统资源异常", HttpStatus.INTERNAL_SERVER_ERROR),
    ThirdServiceError("C0000", "调用第三方服务出错", HttpStatus.INTERNAL_SERVER_ERROR),
    MiddlewareError("C0100", "中间件服务出错", HttpStatus.INTERNAL_SERVER_ERROR),
    ThirdServiceTimeoutError("C0200", "第三方系统执行超时", HttpStatus.INTERNAL_SERVER_ERROR),
    DatabaseError("C0300", "数据库服务出错", HttpStatus.INTERNAL_SERVER_ERROR),
    CacheError("C0HttpStatus.BAD_REQUEST", "缓存服务出错", HttpStatus.INTERNAL_SERVER_ERROR),
    NotificationError("C0500", "通知服务出错", HttpStatus.INTERNAL_SERVER_ERROR),
}