package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamInfoVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamRoleTreeVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamVO
import org.springframework.data.domain.PageRequest

interface TeamService {
    fun getTeamList(): List<TeamInfoVO>

    /**
     * 获取用户创建的队伍列表
     *
     * @param userId
     * @param pageRequest
     * @return
     */
    fun getUserTeams(userId: Long, pageRequest: PageRequest): PagedList<Team, TeamInfoVO>

    /**
     * 获取队伍详情
     *
     * @param teamId
     * @return
     */
    fun getTeamDetail(teamId: Long): TeamVO

    /**
     * 刷新队伍招募角色
     *
     * @param teamId
     * @return
     */
    fun refreshTeamRoles(teamId: Long): TeamVO

    /**
     * 创建队伍
     *
     * @param userId
     * @param dto
     * @return
     */
    fun createTeam(userId: Long, dto: TeamDto): TeamVO

    /**
     * 更新队伍信息
     *
     * @param teamId
     * @param dto
     * @return
     */
    fun updateTeam(teamId: Long, dto: TeamDto): TeamVO

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
    fun getTeamRecruitments(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO>

    /**
     * 创建队伍招募信息
     *
     * @param teamId
     * @param dto
     * @return
     */
    fun createTeamRecruitment(teamId: Long, dto: RecruitmentDto): RecruitmentVO

    /**
     * 修改队伍招募信息
     *
     * @param teamId
     * @param recruitmentId
     * @param dto
     * @return
     */
    fun updateTeamRecruitment(teamId: Long, recruitmentId: Long, dto: RecruitmentDto): RecruitmentVO

    /**
     * 删除队伍招募
     *
     * @param teamId
     * @param recruitmentId
     */
    fun deleteTeamRecruitment(teamId: Long, recruitmentId: Long)

    fun getRoleTree() : List<TeamRoleTreeVO>
}