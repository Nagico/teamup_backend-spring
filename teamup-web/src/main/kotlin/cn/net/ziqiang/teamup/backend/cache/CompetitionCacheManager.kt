package cn.net.ziqiang.teamup.backend.cache

import cn.net.ziqiang.teamup.backend.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class CompetitionCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun setCompetitionCache(competition: cn.net.ziqiang.teamup.backend.pojo.entity.Competition) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.competitionKey(competition.id!!),
            /* value = */ competition,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun getCompetitionCache(id: Long): cn.net.ziqiang.teamup.backend.pojo.entity.Competition? {
        return redisTemplate.opsForValue()[RedisKey.competitionKey(id)] as? cn.net.ziqiang.teamup.backend.pojo.entity.Competition
    }

    fun deleteCompetitionCache(id: Long) {
        redisTemplate.delete(RedisKey.competitionKey(id))
    }

    fun setCompetitionListCache(competitionList: List<cn.net.ziqiang.teamup.backend.pojo.entity.Competition>) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.competitionListKey(),
            /* value = */ competitionList,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun getCompetitionListCache(): List<cn.net.ziqiang.teamup.backend.pojo.entity.Competition>? {
        return redisTemplate.opsForValue()[RedisKey.competitionListKey()] as? List<cn.net.ziqiang.teamup.backend.pojo.entity.Competition>
    }

    fun deleteCompetitionListCache() {
        redisTemplate.delete(RedisKey.competitionListKey())
    }
}