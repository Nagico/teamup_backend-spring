package cn.net.ziqiang.teamup.backend.service.cache

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamRoleTreeVO
import cn.net.ziqiang.teamup.backend.service.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class TeamCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun setTeamCache(team: Team) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.teamKey(team.id!!),
            /* value = */ team,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun getTeamCache(id: Long): Team? {
        return redisTemplate.opsForValue()[RedisKey.teamKey(id)] as? Team
    }

    fun deleteTeamCache(id: Long) {
        redisTemplate.delete(RedisKey.teamKey(id))
    }

    fun getTeamListByUserIdCache(userId: Long): List<Team>? {
        return redisTemplate.opsForValue()[RedisKey.teamListByUserIdKey(userId)] as? List<Team>
    }

    fun setTeamListByUserIdCache(userId: Long, teamList: List<Team>) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.teamListByUserIdKey(userId),
            /* value = */ teamList,
            /* timeout = */ Duration.ofDays(1)
        )
    }

    fun deleteTeamListByUserIdCache(userId: Long) {
        redisTemplate.delete(RedisKey.teamListByUserIdKey(userId))
    }

    fun getTeamRoleTreeCache(): List<TeamRoleTreeVO>? {
        return redisTemplate.opsForValue()[RedisKey.teamRoleTree(0)] as? List<TeamRoleTreeVO>
    }

    fun setTeamRoleTreeCache(teamRoleTree: List<TeamRoleTreeVO>) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.teamRoleTree(0),
            /* value = */ teamRoleTree,
            /* timeout = */ Duration.ofDays(7)
        )
    }
}