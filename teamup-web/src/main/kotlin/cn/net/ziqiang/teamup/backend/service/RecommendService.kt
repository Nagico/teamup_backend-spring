package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.Competition
import cn.net.ziqiang.teamup.backend.pojo.entity.TeamRole

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
}