package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamInfoVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamRoleTreeVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamVO
import cn.net.ziqiang.teamup.backend.service.service.TeamService
import cn.net.ziqiang.teamup.backend.web.annotation.permission.OwnerOrManager
import cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.PermitAll

@RestController
@Tag(name = "队伍")
@RequestMapping("/teams")
class TeamController {
    @Autowired
    private lateinit var teamService: TeamService

    @ActiveUser
    @GetMapping
    @Operation(summary = "获取用户创建的队伍")
    fun getUserTeams(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ): PagedList<Team, TeamInfoVO> {
        val pageRequest = PageRequest.of(page - 1, pageSize)
        return teamService.getUserTeams(SecurityContextUtils.user.id!!, pageRequest)
    }

    @PermitAll
    @GetMapping("/roles")
    @Operation(summary = "获取角色树")
    fun getTeamRoleTree(): List<TeamRoleTreeVO> {
        return teamService.getRoleTree()
    }

    @ActiveUser
    @GetMapping("/{id}")
    @Operation(summary = "获取队伍详情")
    fun getTeamDetail(@PathVariable id: Long): TeamVO {
        return teamService.getTeamDetail(id)
    }

    @ActiveUser
    @PostMapping
    @Operation(summary = "创建队伍")
    fun createTeam(@RequestBody team: TeamDto): TeamVO {
        return teamService.createTeam(SecurityContextUtils.user.id!!, team)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @PutMapping("/{id}")
    @Operation(summary = "修改队伍信息")
    fun updateTeam(@PathVariable id: Long, @RequestBody team: TeamDto): TeamVO {
        return teamService.updateTeam(id, team)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @DeleteMapping("/{id}")
    @Operation(summary = "删除队伍")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteTeam(@PathVariable id: Long) {
        teamService.deleteTeam(id)
    }

    @ActiveUser
    @GetMapping("/{id}/recruitments")
    @Operation(summary = "获取队伍招募信息")
    fun getTeamRecruitments(
        @PathVariable id: Long,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ): PagedList<Recruitment, RecruitmentVO> {
        val pageRequest = PageRequest.of(page - 1, pageSize)
        return teamService.getTeamRecruitments(id, pageRequest)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @PostMapping("/{id}/recruitments")
    @Operation(summary = "创建招募")
    fun createRecruitment(@PathVariable id: Long, @RequestBody recruitment: RecruitmentDto): RecruitmentVO {
        return teamService.createTeamRecruitment(id, recruitment)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @PutMapping("/{id}/recruitments/{recruitmentId}")
    @Operation(summary = "修改招募")
    fun updateRecruitment(
        @PathVariable id: Long,
        @PathVariable recruitmentId: Long,
        @RequestBody recruitment: RecruitmentDto
    ): RecruitmentVO {
        return teamService.updateTeamRecruitment(id, recruitmentId, recruitment)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @DeleteMapping("/{id}/recruitments/{recruitmentId}")
    @Operation(summary = "删除招募")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteRecruitment(@PathVariable id: Long, @PathVariable recruitmentId: Long) {
        teamService.deleteTeamRecruitment(id, recruitmentId)
    }
}