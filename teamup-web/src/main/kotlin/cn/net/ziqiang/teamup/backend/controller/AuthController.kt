package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.vo.auth.*
import cn.net.ziqiang.teamup.backend.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.PermitAll
import javax.validation.Valid

@Tag(name = "鉴权")
@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private lateinit var authService: cn.net.ziqiang.teamup.backend.service.AuthService

    @PermitAll
    @Deprecated("暂停微信登录")
    @Operation(summary = "微信登录")
    @PostMapping("/wechat")
    fun loginWechat(@Valid @RequestBody dto: WechatLoginDto): AuthVO {
        val tokenBean = authService.loginWechat(code = dto.code, iv = dto.iv, encryptedString = dto.encryptedData)

        return AuthVO(tokenBean)
    }

    @PermitAll
    @Deprecated("暂停微信登录")
    @Operation(summary = "微信openid登录")
    @PostMapping("/openid")
    fun loginOpenId(@Valid @RequestBody dto: User): AuthVO {
        if (dto.openid.isBlank()) {
            throw ApiException(ResultType.ParamEmpty)
        }
        val tokenBean = authService.loginOpenid(dto.openid)

        return AuthVO(tokenBean)
    }

    @PermitAll
    @Operation(summary = "手机号密码登录")
    @PostMapping("/login")
    fun loginPassword(@Valid @RequestBody dto: User): AuthVO {
        if (dto.phone.isBlank() || dto.password.isBlank()) {
            throw ApiException(ResultType.ParamEmpty)
        }
        return AuthVO(authService.loginPassword(dto.phone, dto.password))
    }

    @PermitAll
    @Operation(summary = "刷新token")
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody dto: AuthVO): AuthVO {
        if (dto.refresh.isBlank()) {
            throw ApiException(ResultType.ParamEmpty)
        }
        val tokenBean = authService.refreshToken(refreshToken = dto.refresh)
        return AuthVO(tokenBean)
    }
}