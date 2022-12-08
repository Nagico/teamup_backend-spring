package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import cn.net.ziqiang.teamup.backend.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.vo.team.TeamRoleTreeVO
import cn.net.ziqiang.teamup.backend.service.TeamService
import cn.net.ziqiang.teamup.backend.util.annotation.permission.OwnerOrManager
import cn.net.ziqiang.teamup.backend.util.annotation.user.ActiveUser
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
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
    ): PagedList<Team, Team> {
        val userId = SecurityContextUtils.user.id!!
        val pageRequest = PageRequest.of(page - 1, pageSize)
        return teamService.getUserTeams(userId, pageRequest)
    }

    @PermitAll
    @GetMapping("/roles")
    @Operation(summary = "获取角色树")
    fun getTeamRoleTree(): List<TeamRoleTreeVO> {
        return teamService.getRoleTree()
    }

    @PermitAll
    @GetMapping("/{id}")
    @Operation(summary = "获取队伍详情")
    fun getTeamDetail(@PathVariable id: Long): Team {
        return teamService.getTeamDetail(SecurityContextUtils.userIdOrNull, id)
    }

    @ActiveUser
    @GetMapping("/{id}/refresh")
    @Operation(summary = "刷新队伍招募角色信息")
    fun refreshTeamRoles(@PathVariable id: Long): Team {
        return teamService.refreshTeamRoles(id)
    }

    @ActiveUser
    @PostMapping
    @Operation(summary = "创建队伍")
    fun createTeam(@RequestBody team: Team): Team {
        return teamService.createTeam(SecurityContextUtils.user.id!!, team)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @PutMapping("/{id}")
    @Operation(summary = "修改队伍信息")
    fun updateTeam(@PathVariable id: Long, @RequestBody team: Team): Team {
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
    ): PagedList<Recruitment, Recruitment> {
        val pageRequest = PageRequest.of(page - 1, pageSize)
        return teamService.getTeamRecruitments(id, pageRequest)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @PostMapping("/{id}/recruitments")
    @Operation(summary = "创建招募")
    fun createRecruitment(@PathVariable id: Long, @RequestBody recruitment: Recruitment): Recruitment {
        return teamService.createTeamRecruitment(id, recruitment)
    }

    @ActiveUser
    @OwnerOrManager("team")
    @PutMapping("/{id}/recruitments/{recruitmentId}")
    @Operation(summary = "修改招募")
    fun updateRecruitment(
        @PathVariable id: Long,
        @PathVariable recruitmentId: Long,
        @RequestBody recruitment: Recruitment
    ): Recruitment {
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