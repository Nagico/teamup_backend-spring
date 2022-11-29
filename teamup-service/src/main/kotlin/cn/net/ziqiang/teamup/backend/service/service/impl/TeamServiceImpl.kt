package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Tag
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.entity.TeamMember
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamInfoVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamVO
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRepository
import cn.net.ziqiang.teamup.backend.service.service.CompetitionService
import cn.net.ziqiang.teamup.backend.service.service.TagService
import cn.net.ziqiang.teamup.backend.service.service.TeamService
import cn.net.ziqiang.teamup.backend.service.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeamServiceImpl : TeamService {
    @Autowired
    private lateinit var teamRepository: TeamRepository
    @Autowired
    private lateinit var competitionService: CompetitionService
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var tagService: TagService

    private fun getTeam(teamId: Long): Team {
        return teamRepository.findById(teamId).orElseThrow { ApiException(ResultType.ResourceNotFound) }.checkPermission()
    }

    override fun getUserTeams(userId: Long, pageRequest: PageRequest): PagedList<Team, TeamInfoVO> {
        val teams = teamRepository.findAllByLeaderId(userId, pageRequest)
        return PagedList(teams)
    }

    override fun getTeamDetail(teamId: Long): TeamVO {
        return TeamVO(getTeam(teamId))
    }

    @Transactional
    override fun createTeam(userId: Long, dto: TeamDto): TeamVO {
        val competition = competitionService.getCompetitionById(dto.competition!!).takeIf { it.verified }
                ?: throw ApiException(ResultType.ParamValidationFailed, "比赛暂未通过审核")

        val team = Team(
            name = dto.name!!,
            competition = competition,
            description = dto.description!!,
            tags = tagService.getOrCreateTags(dto.tags!!) as MutableList<Tag>,
            leader = userService.getUserById(userId),
            members = dto.members as MutableList<TeamMember>,
            recruiting = dto.recruiting ?: false
        )

        return TeamVO(teamRepository.save(team))
    }

    override fun updateTeam(teamId: Long, dto: TeamDto): TeamVO {
        val team = getTeam(teamId)
        dto.name?.let { team.name = it }
        dto.description?.let { team.description = it }
        dto.tags?.let { team.tags = tagService.getOrCreateTags(it) as MutableList<Tag> }
        dto.members?.let { team.members = it as MutableList<TeamMember> }
        dto.recruiting?.let { team.recruiting = it }

        return TeamVO(teamRepository.save(team))
    }

    override fun deleteTeam(teamId: Long) {
        val team = getTeam(teamId)
        teamRepository.delete(team)
    }
}