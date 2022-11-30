package cn.net.ziqiang.teamup.backend.service.cache


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class OssCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    private val _keyPrefix = "oss:pubkey:callback:url="

    //OSS callback 验证 缓存

    fun setKeyCache(url: String, content: String) {
        redisTemplate.opsForValue().set(
            /* key = */ "$_keyPrefix:$url",
            /* value = */ content
        )
    }

    fun getKeyCache(url: String): String? {
        return redisTemplate.opsForValue()["$_keyPrefix$url"] as? String
    }
}