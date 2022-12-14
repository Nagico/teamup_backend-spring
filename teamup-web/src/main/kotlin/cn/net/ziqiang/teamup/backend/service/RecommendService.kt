package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.Competition
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.entity.TeamRole
import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import org.springframework.data.domain.PageRequest

interface RecommendService {
    /**
     * 获取用户收藏的比赛
     *
     * @param userId
     * @return
     */
    fun getUserSubscribeCompetition(userId: Long): List<Competition>

    /**
     * 添加比赛收藏
     *
     * @param userId
     * @param competitionId
     */
    fun addUserSubscribeCompetition(userId: Long, competitionId: Long)

    /**
     * 删除比赛收藏
     *
     * @param userId
     * @param competitionId
     */
    fun deleteUserSubscribeCompetition(userId: Long, competitionId: Long)

    /**
     * 检查用户是否订阅比赛
     *
     * @param userId
     * @param competitionId
     * @return
     */
    fun checkUserSubscribeCompetition(userId: Long, competitionId: Long): Boolean

    /**
     * 获取用户收藏的角色
     *
     * @param userId
     * @return
     */
    fun getUserSubscribeRole(userId: Long): List<TeamRole>

    /**
     * 添加角色收藏
     *
     * @param userId
     * @param roleId
     */
    fun addUserSubscribeRole(userId: Long, roleId: Long)

    /**
     * 删除角色收藏
     *
     * @param userId
     * @param roleId
     */
    fun deleteUserSubscribeRole(userId: Long, roleId: Long)

    /**
     * 查询用户队伍收藏列表
     *
     * @param userId
     * @return
     */
    fun getUserFavoriteTeam(userId: Long, pageRequest: PageRequest): PagedList<Team, Team>

    /**
     * 添加队伍收藏
     *
     * @param userId
     * @param teamId
     */
    fun addUserFavoriteTeam(userId: Long, teamId: Long)

    /**
     * 删除队伍收藏
     *
     * @param userId
     * @param teamId
     */
    fun deleteUserFavoriteTeam(userId: Long, teamId: Long)

    /**
     * 检查用户是否收藏队伍、感兴趣、不感兴趣
     *
     * @param userId
     * @param team
     * @return
     */
    fun checkUserTeam(userId: Long, team: Team)

    /**
     * 检查用户是否收藏队伍
     *
     * @param userId
     * @param teams
     */
    fun checkUserFavoriteTeams(userId: Long, teams: List<Team>)

    /**
     * 添加用户感兴趣队伍
     *
     * @param userId
     * @param teamId
     */
    fun addUserInterestingTeam(userId: Long, teamId: Long)

    /**
     * 删除用户感兴趣队伍
     *
     * @param userId
     * @param teamId
     */
    fun deleteUserInterestingTeam(userId: Long, teamId: Long)

    /**
     * 检查用户不感兴趣队伍
     *
     * @param userId
     * @param teamId
     */
    fun addUserUninterestingTeam(userId: Long, teamId: Long)

    /**
     * 删除用户不感兴趣队伍
     *
     * @param userId
     * @param teamId
     */
    fun deleteUserUninterestingTeam(userId: Long, teamId: Long)
}