package cn.net.ziqiang.teamup.backend.service.cache

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.service.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class CompetitionCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun setCompetitionCache(competition: Competition) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.competitionKey(competition.id!!),
            /* value = */ competition,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun getCompetitionCache(id: Long): Competition? {
        return redisTemplate.opsForValue()[RedisKey.competitionKey(id)] as? Competition
    }

    fun deleteCompetitionCache(id: Long) {
        redisTemplate.delete(RedisKey.competitionKey(id))
    }

    fun setCompetitionListCache(competitionList: List<Competition>) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.competitionListKey(),
            /* value = */ competitionList,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun getCompetitionListCache(): List<Competition>? {
        return redisTemplate.opsForValue()[RedisKey.competitionListKey()] as? List<Competition>
    }

    fun deleteCompetitionListCache() {
        redisTemplate.delete(RedisKey.competitionListKey())
    }
}