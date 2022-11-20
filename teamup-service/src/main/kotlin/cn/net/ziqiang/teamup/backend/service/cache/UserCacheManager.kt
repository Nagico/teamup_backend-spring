package cn.net.ziqiang.teamup.backend.service.cache


import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.service.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class UserCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    //用户缓存 key存id，value存用户对应的JSON字符串，默认1小时过期

    fun setUserCache(user: User) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.userKey(userId = user.id!!),
            /* value = */ user,
            /* timeout = */ Duration.ofHours(1)
        )
    }

    fun getUserCache(userId: Long): User? {
        return redisTemplate.opsForValue()[RedisKey.userKey(userId = userId)] as? User
    }
}