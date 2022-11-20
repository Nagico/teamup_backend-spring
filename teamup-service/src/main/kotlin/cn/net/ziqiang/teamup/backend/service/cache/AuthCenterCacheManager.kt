package cn.net.ziqiang.teamup.backend.service.cache

import cn.net.ziqiang.teamup.backend.common.bean.auth.TokenBean
import cn.net.ziqiang.teamup.backend.service.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class AuthCenterCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    //authToken 7天过期 key通过userId区分
    //refreshToken 30天过期，key通过userId区分

    /**
     * 设置鉴权token
     * @param userId
     * @param token
     */
    fun setAuthToken(userId: Long, token: String) {
        redisTemplate.opsForValue().set(
            RedisKey.authTokenKey(userId = userId),
            token,
            Duration.ofDays(7)
        )
    }

    /**
     * 获取鉴权token
     * @param userId
     * @return
     */
    fun getAuthToken(userId: Long): String? {
        return (redisTemplate.opsForValue()[RedisKey.authTokenKey(userId = userId)] as? String)?.ifEmpty { null }
    }

    /**
     * 设置刷新token
     * @param refreshToken
     */
    fun setRefreshToken(refreshToken: String) {
        redisTemplate.opsForValue().set(
            RedisKey.refreshToken(refreshToken = refreshToken),
            refreshToken,
            Duration.ofDays(30)
        )
    }

    /**
     * 删除刷新token
     * @param refreshToken
     */
    fun deleteRefreshToken(refreshToken: String) {
        redisTemplate.delete(RedisKey.refreshToken(refreshToken = refreshToken))
    }

    /**
     * 获取刷新token
     * @return
     */
    fun getRefreshToken(refreshToken: String): String? {
        return (redisTemplate.opsForValue()[RedisKey.refreshToken(refreshToken = refreshToken)] as? String)
            ?.ifEmpty { null }
    }

    fun setToken(userId: Long, tokenBean: TokenBean) {
        setAuthToken(userId = userId, token = tokenBean.access)
        setRefreshToken(refreshToken = tokenBean.refresh)
    }

}