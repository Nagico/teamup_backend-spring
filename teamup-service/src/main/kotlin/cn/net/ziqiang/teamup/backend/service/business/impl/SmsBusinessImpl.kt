package cn.net.ziqiang.teamup.backend.service.business.impl

import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j
import cn.net.ziqiang.teamup.backend.common.annotation.Slf4j.Companion.logger
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.service.annotation.Business
import cn.net.ziqiang.teamup.backend.service.business.SmsBusiness
import cn.net.ziqiang.teamup.backend.service.cache.SmsCacheManager
import cn.net.ziqiang.teamup.backend.service.properties.SmsProperties
import com.aliyun.auth.credentials.Credential
import com.aliyun.auth.credentials.provider.StaticCredentialProvider
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse
import darabonba.core.client.ClientOverrideConfiguration
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CompletableFuture

@Slf4j
@Business
class SmsBusinessImpl : SmsBusiness {
    @Autowired
    private lateinit var smsProperties: SmsProperties

    @Autowired
    private lateinit var smsCacheManager: SmsCacheManager

    private var client: AsyncClient? = null
        get() {
            if (field == null) {
                synchronized(this) {
                    if (field == null) {
                        val provider = StaticCredentialProvider.create(
                            Credential.builder()
                                .accessKeyId(smsProperties.accessKeyId)
                                .accessKeySecret(smsProperties.accessKeySecret)
                                .build()
                        )

                        field = AsyncClient.builder()
                            .region("cn-hangzhou") // Region ID
                            .credentialsProvider(provider)
                            .overrideConfiguration(
                                ClientOverrideConfiguration.create()
                                    .setEndpointOverride("dysmsapi.aliyuncs.com")
                            )
                            .build()
                    }
                }
            }
            return field
        }

    override fun sendVerifyCode(phone: String, code: String) {
        checkThrottle(phone)
        val sendSmsRequest = SendSmsRequest.builder()
            .signName(smsProperties.signName)
            .templateCode(smsProperties.getTemplateCodeByName("verifyCode"))
            .phoneNumbers(phone)
            .templateParam("{\"code\":\"${code}\"}")
            .build()

        logger.info("$phone send sms success: $code")

//        val response: CompletableFuture<SendSmsResponse> = client!!.sendSms(sendSmsRequest)
//        response.thenAccept { _: SendSmsResponse? ->
//            logger.info("$phone send sms success: $code")
//            smsCacheManager.setSmsVerifyCode(phone, code)
//        }.exceptionally { throwable: Throwable ->
//            logger.error("$phone send sms failed: ${throwable.message}")
//            null
//        }
    }

    // region Utils
    private fun checkThrottle(phone: String) {
        if (smsCacheManager.getPhoneStatus(phone) != null) {
            throw ApiException(ResultType.APIThrottled, "短信发送过于频繁，请稍后再试")
        }
        smsCacheManager.setPhoneStatus(phone)
    }

    // endregion
}

