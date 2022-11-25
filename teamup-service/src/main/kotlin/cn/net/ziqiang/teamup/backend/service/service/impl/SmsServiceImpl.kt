package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.business.SmsBusiness
import cn.net.ziqiang.teamup.backend.service.cache.SmsCacheManager
import cn.net.ziqiang.teamup.backend.service.service.SmsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SmsServiceImpl : SmsService {
    @Autowired
    private lateinit var smsBusiness: SmsBusiness
    @Autowired
    private lateinit var smsCacheManager: SmsCacheManager

    private val phoneRex = Regex("^1[3-9]\\d{9}$")

    override fun sendVerifyCode(phone: String) {
        if (phone.isBlank()) {
            throw ApiException(ResultType.ParamValidationFailed, "手机号不能为空")
        }

        if (!phoneRex.matches(phone)) {
            throw ApiException(ResultType.ParamValidationFailed, "手机号格式不正确")
        }

        val verifyCode = Math.random().toString().substring(2, 8)
        smsBusiness.sendVerifyCode(phone, verifyCode)
        smsCacheManager.setVerifyCode(phone, verifyCode)
    }

    override fun checkVerifyCode(phone: String, code: String): Boolean {
        val verifyCode = smsCacheManager.getVerifyCode(phone)
        if (code == verifyCode) {
            smsCacheManager.deleteVerifyCode(phone)
            return true
        }
        return false
    }
}