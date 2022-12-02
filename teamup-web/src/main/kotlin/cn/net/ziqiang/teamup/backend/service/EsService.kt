package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.es.TeamDoc

interface EsService {
    /**
     * 添加队伍索引
     *
     * @param team
     */
    fun addTeamDoc(team: Team)

    /**
     * 更新队伍索引
     *
     * @param team
     */
    fun updateTeamDoc(team: Team)

    /**
     * 删除队伍索引
     *
     * @param teamId
     */
    fun deleteTeamDoc(teamId: Long)

    /**
     * 获取全部索引队伍
     *
     * @return
     */
    fun getAllTeamDocs(): List<TeamDoc>

    /**
     * 根据id获取队伍索引
     *
     * @param teamId
     * @return
     */
    fun getTeamDocById(teamId: Long): TeamDoc

    /**
     * 根据比赛名称获取队伍索引
     *
     * @param competition
     * @return
     */
    fun getTeamDocListByCompetition(competition: String): List<TeamDoc>

    /**
     * 根据角色获取队伍索引
     *
     * @param role
     * @return
     */
    fun getTeamDocListByRole(role: String): List<TeamDoc>

    /**
     * 根据搜索词获取队伍索引
     *
     * @param search
     * @return
     */
    fun getTeamDocListBySearch(search: String): List<TeamDoc>

    /**
     * 重构队伍索引
     *
     * @param items
     */
    fun rebuildIndex(items: List<TeamDoc>)
}