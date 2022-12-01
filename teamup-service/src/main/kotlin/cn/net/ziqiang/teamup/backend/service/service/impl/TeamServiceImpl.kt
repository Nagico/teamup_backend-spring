package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Tag
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.common.pojo.entity.TeamMember
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.*
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRoleRepository
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRepository
import cn.net.ziqiang.teamup.backend.service.cache.TeamCacheManager
import cn.net.ziqiang.teamup.backend.service.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.concurrent.thread

@Service
class TeamServiceImpl : TeamService {
    @Autowired
    private lateinit var teamRepository: TeamRepository
    @Autowired
    private lateinit var teamRoleRepository: TeamRoleRepository
    @Autowired
    private lateinit var competitionService: CompetitionService
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var tagService: TagService
    @Autowired
    private lateinit var recruitmentService: RecruitmentService
    @Autowired
    private lateinit var teamCacheManager: TeamCacheManager


    private fun getTeam(teamId: Long, useCache: Boolean = false): Team {
        return if (useCache) {
            teamCacheManager.getTeamCache(teamId) ?: run {
                teamRepository.findById(teamId).orElseThrow { ApiException(ResultType.ResourceNotFound) }.apply {
                    teamCacheManager.setTeamCache(this)
                }.checkPermission()
            }
        } else {
            teamRepository.findById(teamId).orElseThrow { ApiException(ResultType.ResourceNotFound) }.checkPermission()
        }
    }

    override fun getTeamList(): List<TeamInfoVO> {
        return teamRepository.findAll().map { TeamInfoVO(it) }
    }

    override fun getUserTeams(userId: Long, pageRequest: PageRequest): PagedList<Team, TeamInfoVO> {
        val cachedList = teamCacheManager.getTeamListByUserIdCache(userId)
        return if (cachedList != null) {
            PagedList(cachedList, pageRequest) { TeamInfoVO(it) }
        } else {
            val teams = teamRepository.findAllByLeaderId(userId, pageRequest)
            thread {
                teamCacheManager.setTeamListByUserIdCache(userId, teamRepository.findAllByLeaderId(userId))
            }
            PagedList(teams) { TeamInfoVO(it) }
        }
    }

    override fun getTeamDetail(teamId: Long): TeamVO {
        return TeamVO(getTeam(teamId, useCache = true))
    }

    override fun refreshTeamRoles(teamId: Long): TeamVO {
        val team = getTeam(teamId, useCache = false)
        val roleSet = recruitmentService.getRecruitmentListByTeamId(teamId).map { TeamRoleVO(it.role!!) }.toSet()
        team.recruitmentRoles = roleSet as MutableSet<TeamRoleVO>
        teamRepository.save(team)
        teamCacheManager.setTeamCache(team)
        return TeamVO(team)
    }

    @Transactional
    override fun createTeam(userId: Long, dto: TeamDto): TeamVO {
        val competition = competitionService.getCompetitionById(dto.competition!!).takeIf { it.verified }
                ?: throw ApiException(ResultType.ParamValidationFailed, "比赛暂未通过审核")

        val members = dto.members!!.map {
            TeamMember(
                roles = teamRoleRepository.getAllByIdIn(it.roles!!).map { role -> TeamRoleVO(role) } as MutableList<TeamRoleVO>,
                faculty = it.faculty,
                description = it.description,
            )
        }

        val team = Team(
            name = dto.name!!,
            competition = competition,
            description = dto.description!!,
            tags = tagService.getOrCreateTags(dto.tags!!) as MutableList<Tag>,
            leader = userService.getUserById(userId),
            members = members as MutableList<TeamMember>,
            recruiting = dto.recruiting ?: false
        )

        return TeamVO(teamRepository.save(team)).apply {
            thread {
                teamCacheManager.setTeamCache(team)
                teamCacheManager.setTeamListByUserIdCache(userId, teamRepository.findAllByLeaderId(userId))
            }
        }
    }

    override fun updateTeam(teamId: Long, dto: TeamDto): TeamVO {
        val team = getTeam(teamId)

        val members = dto.members?.map {
            TeamMember(
                roles = teamRoleRepository.getAllByIdIn(it.roles!!).map { role -> TeamRoleVO(role) } as MutableList<TeamRoleVO>,
                faculty = it.faculty,
                description = it.description,
            )
        }

        dto.name?.let { team.name = it }
        dto.description?.let { team.description = it }
        dto.tags?.let { team.tags = tagService.getOrCreateTags(it) as MutableList<Tag> }
        members?.let { team.members = it as MutableList<TeamMember> }
        dto.recruiting?.let { team.recruiting = it }

        return TeamVO(teamRepository.save(team)).apply {
            val userId = team.leader!!.id!!
            thread {
                teamCacheManager.setTeamCache(team)
                teamCacheManager.setTeamListByUserIdCache(userId, teamRepository.findAllByLeaderId(userId))
            }
        }
    }

    override fun deleteTeam(teamId: Long) {
        val userId = getTeam(teamId).leader!!.id!!
        thread {
            teamRepository.deleteById(teamId)
            teamCacheManager.deleteTeamCache(teamId)
            teamCacheManager.deleteTeamListByUserIdCache(userId)
        }
    }

    override fun getTeamRecruitments(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO> {
        val team = getTeam(teamId, useCache = true)
        return recruitmentService.getRecruitmentListByTeamId(team.id!!, pageRequest)
    }

    override fun createTeamRecruitment(teamId: Long, dto: RecruitmentDto): RecruitmentVO {
        val team = getTeam(teamId, useCache = false).apply {
            dto.team = this
        }

        return recruitmentService.createRecruitment(dto).apply {
            if (!team.recruitmentRoles.contains(this.role)) {
                team.recruitmentRoles.add(this.role)
                teamRepository.save(team)
                teamCacheManager.setTeamCache(team)
            }
        }
    }

    override fun updateTeamRecruitment(teamId: Long, recruitmentId: Long, dto: RecruitmentDto): RecruitmentVO {
        val team = getTeam(teamId, useCache = false).apply {
            dto.team = this
        }

        val oldRole = recruitmentService.getRecruitmentById(recruitmentId).role

        return recruitmentService.updateRecruitment(recruitmentId, dto).apply {
            var refresh = false

            if (team.recruitmentRoles.contains(oldRole)) {
                team.recruitmentRoles.remove(oldRole)
                refresh = true
            }

            if (!team.recruitmentRoles.contains(this.role)) {
                team.recruitmentRoles.add(this.role)
                refresh = true
            }

            if (refresh) {
                teamRepository.save(team)
                teamCacheManager.setTeamCache(team)
            }
        }
    }

    override fun deleteTeamRecruitment(teamId: Long, recruitmentId: Long) {
        val team = getTeam(teamId, useCache = false)

        val oldRole = recruitmentService.getRecruitmentById(recruitmentId).role

        recruitmentService.deleteRecruitment(recruitmentId).apply {
            if (team.recruitmentRoles.contains(oldRole)) {
                team.recruitmentRoles.remove(oldRole)
                teamRepository.save(team)
                teamCacheManager.setTeamCache(team)
            }
        }
    }

    override fun getRoleTree() : List<TeamRoleTreeVO> {
        return teamCacheManager.getTeamRoleTreeCache() ?: run {
            val roles = teamRoleRepository.findAll()

            val res = mutableListOf<TeamRoleTreeVO>()

            roles.forEach {
                if (it.pid == null) {
                    res.add(TeamRoleTreeVO(it))
                } else {
                    val parent = res.find { role -> role.id == it.pid }!!
                    if (parent.children == null) parent.children = mutableListOf()

                    parent.children!!.add(TeamRoleTreeVO(it))
                }
            }

            res.apply {
                teamCacheManager.setTeamRoleTreeCache(this)
            }
        }
    }
}