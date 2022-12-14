package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import cn.net.ziqiang.teamup.backend.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.vo.team.TeamRoleTreeVO
import org.springframework.data.domain.PageRequest

interface TeamService {
    /**
     * 搜索队伍
     *
     * @param competition
     * @param role
     * @param searchText
     * @param pageRequest
     * @return
     */
    fun searchTeams(
        competition: String?,
        role: String?,
        searchText: String?,
        pageRequest: PageRequest,
        userId: Long?
    ): PagedList<Team>

    /**
     * 获取用户创建的队伍列表
     *
     * @param userId
     * @param pageRequest
     * @return
     */
    fun getUserTeams(userId: Long, pageRequest: PageRequest): PagedList<Team>

    fun getTeamCountByUserId(userId: Long): Long

    /**
     * 获取队伍详情
     *
     * @param teamId
     * @return
     */
    fun getTeamDetail(userId: Long?, teamId: Long): Team

    /**
     * 刷新队伍招募角色
     *
     * @param teamId
     * @return
     */
    fun refreshTeamRoles(teamId: Long): Team

    /**
     * 创建队伍
     *
     * @param userId
     * @param dto
     * @return
     */
    fun createTeam(userId: Long, dto: Team): Team

    /**
     * 更新队伍信息
     *
     * @param teamId
     * @param dto
     * @return
     */
    fun updateTeam(teamId: Long, dto: Team): Team

    /**
     * 删除队伍
     *
     * @param teamId
     */
    fun deleteTeam(teamId: Long)

    /**
     * 获取队伍待招募列表
     *
     * @param teamId
     * @param pageRequest
     * @return
     */
    fun getTeamRecruitments(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment>

    /**
     * 创建队伍招募信息
     *
     * @param teamId
     * @param dto
     * @return
     */
    fun createTeamRecruitment(teamId: Long, dto: Recruitment): Recruitment

    /**
     * 修改队伍招募信息
     *
     * @param teamId
     * @param recruitmentId
     * @param dto
     * @return
     */
    fun updateTeamRecruitment(teamId: Long, recruitmentId: Long, dto: Recruitment): Recruitment

    /**
     * 删除队伍招募
     *
     * @param teamId
     * @param recruitmentId
     */
    fun deleteTeamRecruitment(teamId: Long, recruitmentId: Long)

    /**
     * 获取队伍角色树
     *
     * @return
     */
    fun getRoleTree() : List<TeamRoleTreeVO>

    /**
     * 重建队伍索引
     *
     */
    fun rebuildTeamDoc()

    /**
     * 获取队伍推荐人员
     *
     * @param teamId
     * @param pageRequest
     * @return
     */
    fun recommendUsers(teamId: Long, pageRequest: PageRequest): PagedList<User>
}