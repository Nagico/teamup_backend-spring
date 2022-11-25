package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pojo.vo.auth.*
import cn.net.ziqiang.teamup.backend.service.service.SmsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.annotation.security.PermitAll

@Tag(name = "短信服务")
@RestController
@RequestMapping("/sms")
class SmsController {
    @Autowired
    private lateinit var smsService: SmsService

    @PermitAll
    @GetMapping("/verifyCodes")
    @Operation(summary = "发送短信验证码")
    fun sendVerifyCode(@RequestParam(defaultValue = "") phone: String): Map<String, String> {
        smsService.sendVerifyCode(phone)
        return mapOf("status" to "success")
    }
}