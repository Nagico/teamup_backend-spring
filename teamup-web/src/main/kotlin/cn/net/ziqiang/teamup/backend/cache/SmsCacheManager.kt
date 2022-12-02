package cn.net.ziqiang.teamup.backend.cache

import cn.net.ziqiang.teamup.backend.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class SmsCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    /**
     * 获取短信验证码
     *
     * @param phone
     * @return
     */
    fun getVerifyCode(phone: String): String? {
        return (redisTemplate.opsForValue()[RedisKey.smsVerifyCodeKey(phone = phone)] as? String)?.ifEmpty { null }
    }

    /**
     * 设置短信验证码
     *
     * @param phone
     * @param code
     */
    fun setVerifyCode(phone: String, code: String) {
        redisTemplate.opsForValue().set(
            RedisKey.smsVerifyCodeKey(phone = phone),
            code,
            Duration.ofMinutes(3)
        )
    }

    /**
     * 删除短信验证码
     *
     * @param phone
     */
    fun deleteVerifyCode(phone: String) {
        redisTemplate.delete(RedisKey.smsVerifyCodeKey(phone = phone))
    }

    /**
     * 获取手机号状态
     *
     * @param phone
     * @return
     */
    fun getPhoneStatus(phone: String): String? {
        return (redisTemplate.opsForValue()[RedisKey.phoneStatusKey(phone = phone)] as? String)?.ifEmpty { null }
    }

    /**
     * 设置手机号状态
     *
     * @param phone
     */
    fun setPhoneStatus(phone: String) {
        redisTemplate.opsForValue().set(
            RedisKey.phoneStatusKey(phone = phone),
            "1",
            Duration.ofMinutes(1)
        )
    }

}