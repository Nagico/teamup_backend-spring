package cn.net.ziqiang.teamup.backend.service.cache

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.service.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class RecruitmentCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun setRecruitmentCache(recruitment: Recruitment) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.recruitmentKey(recruitment.id!!),
            /* value = */ recruitment,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun getRecruitmentCache(id: Long): Recruitment? {
        return redisTemplate.opsForValue()[RedisKey.recruitmentKey(id)] as? Recruitment
    }
}