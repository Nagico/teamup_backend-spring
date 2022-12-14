package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import cn.net.ziqiang.teamup.backend.pojo.entity.*
import cn.net.ziqiang.teamup.backend.pojo.es.TeamDoc
import cn.net.ziqiang.teamup.backend.pojo.vo.team.*
import cn.net.ziqiang.teamup.backend.util.getInfo
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRoleRepository
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRepository
import cn.net.ziqiang.teamup.backend.cache.TeamCacheManager
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.service.CompetitionService
import cn.net.ziqiang.teamup.backend.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
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
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var tagService: TagService
    @Autowired
    private lateinit var recruitmentService: RecruitmentService
    @Autowired
    private lateinit var teamCacheManager: TeamCacheManager
    @Autowired
    private lateinit var esService: EsService
    @Autowired
    private lateinit var recommendService: RecommendService


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

    override fun searchTeams(
        competition: String?,
        role: String?,
        searchText: String?,
        pageRequest: PageRequest,
        userId: Long?
    ): PagedList<Team> {
        val idList =  if (competition != null || role != null) {
            esService.getTeamDocListByCompetitionAndRole(competition, role).map { it.id!! }
        } else if (searchText != null) {
            esService.getTeamDocListBySearch(searchText).map { it.id!! }
        } else {  // 获取所有队伍（推荐模式）
            var originIds = esService.getAllTeamDocs().map { it.id!! }
            if (userId != null) {
                val recommendIds = recommendService.getRecommendTeamIds(userId)
                originIds = originIds.filter { !recommendIds.contains(it) }
                originIds = recommendIds + originIds
            }
            originIds
        }
        val result = PagedList(teamRepository.findAllByIdsOrdered(idList, pageRequest)) {
            it.apply { it.leader?.getInfo() }
        }

        return result.apply {
            if (userId != null) {
                recommendService.checkUserFavoriteTeams(userId, this.results)
            }
        }
    }

    override fun getUserTeams(userId: Long, pageRequest: PageRequest): PagedList<Team> {
        val cachedList = teamCacheManager.getTeamListByUserIdCache(userId)

        val result = if (cachedList != null) {
            PagedList(cachedList, pageRequest) { it.apply { it.leader!!.getInfo() } }
        } else {
            val teams = teamRepository.findAllByLeaderId(userId, pageRequest)
            thread {
                teamCacheManager.setTeamListByUserIdCache(userId, teamRepository.findAllByLeaderId(userId))
            }
            PagedList(teams) { it.apply { it.leader!!.getInfo() } }
        }

        return result.apply {
            recommendService.checkUserFavoriteTeams(userId, results)
        }
    }

    override fun getTeamCountByUserId(userId: Long): Long {
        return teamRepository.countByLeaderId(userId)
    }

    override fun getTeamDetail(userId: Long?, teamId: Long): Team {
        return getTeam(teamId, useCache = true).apply {
            leader?.getInfo()
            recruitments = recruitmentService.getRecruitmentListByTeamId(id!!)
            if (userId != null) {
                recommendService.checkUserTeam(userId, this)
            }
        }
    }

    override fun refreshTeamRoles(teamId: Long): Team {
        val team = getTeam(teamId, useCache = false)
        val roleSet = recruitmentService.getRecruitmentListByTeamId(teamId).map { it.role!! }.toSet()
        team.roles = roleSet as MutableSet<TeamRole>
        teamRepository.save(team)
        teamCacheManager.setTeamCache(team)
        return team.apply {
            leader?.getInfo()
        }
    }

    @Transactional
    override fun createTeam(userId: Long, dto: Team): Team {
        val competition = competitionService.getCompetitionById(dto.competition!!.id!!, null)

        val members = dto.members.map {
            TeamMember(
                roles = teamRoleRepository.getAllByIdIn(it.roles.map { role -> role.id!! }) as MutableList<TeamRole>,
                faculty = it.faculty,
                description = it.description,
            )
        }

        val team = Team(
            name = dto.name!!,
            competition = competition,
            description = dto.description!!,
            tags = tagService.getOrCreateTags(dto.tags.map { it.content!! }) as MutableList<Tag>,
            leader = userService.getUserById(userId),
            members = members as MutableList<TeamMember>,
            recruiting = dto.recruiting,
            createTime = Date(),
        )

        val savedTeam = teamRepository.save(team)

        for (recruitment in dto.recruitments!!) {
            recruitment.team = savedTeam
            createTeamRecruitment(savedTeam.id!!, recruitment)
        }

        return savedTeam.apply {
            leader?.getInfo()
            thread {
                teamCacheManager.setTeamCache(team)
                teamCacheManager.setTeamListByUserIdCache(userId, teamRepository.findAllByLeaderId(userId))
                esService.addTeamDoc(team)
            }
        }
    }

    override fun updateTeam(teamId: Long, dto: Team): Team {
        val team = getTeam(teamId)

        val members = dto.members.map {
            TeamMember(
                roles = teamRoleRepository.getAllByIdIn(it.roles.map { role -> role.id!! }) as MutableList<TeamRole>,
                faculty = it.faculty,
                description = it.description,
            )
        }

        dto.name?.let { team.name = it }
        dto.description?.let { team.description = it }
        dto.tags.let { team.tags = tagService.getOrCreateTags(it.map{ tag -> tag.content!!} ) as MutableList<Tag> }
        members.let { team.members = it as MutableList<TeamMember> }
        dto.recruiting.let { team.recruiting = it }

        return teamRepository.save(team).apply {
            leader?.getInfo()
            recruitments = recruitmentService.getRecruitmentListByTeamId(id!!)
            val userId = team.leader!!.id!!
            thread {
                teamCacheManager.setTeamCache(team)
                teamCacheManager.setTeamListByUserIdCache(userId, teamRepository.findAllByLeaderId(userId))
                esService.updateTeamDoc(team)
            }
        }
    }

    override fun deleteTeam(teamId: Long) {
        val userId = getTeam(teamId).leader!!.id!!
        thread {
            teamRepository.deleteById(teamId)
            teamCacheManager.deleteTeamCache(teamId)
            teamCacheManager.deleteTeamListByUserIdCache(userId)
            esService.deleteTeamDoc(teamId)
        }
    }

    override fun getTeamRecruitments(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment> {
        val team = getTeam(teamId, useCache = true)
        return recruitmentService.getRecruitmentListByTeamId(team.id!!, pageRequest)
    }

    override fun createTeamRecruitment(teamId: Long, dto: Recruitment): Recruitment {
        val team = getTeam(teamId, useCache = false).apply {
            dto.team = this
        }

        return recruitmentService.createRecruitment(dto).apply {
            if (!team.roles.contains(this.role!!)) {
                team.roles.add(this.role!!)
                teamRepository.save(team)
                teamCacheManager.setTeamCache(team)
            }
            esService.updateTeamDoc(team)
        }
    }

    override fun updateTeamRecruitment(teamId: Long, recruitmentId: Long, dto: Recruitment): Recruitment {
        val team = getTeam(teamId, useCache = false).apply {
            dto.team = this
        }

        val oldRole = recruitmentService.getRecruitmentById(recruitmentId).role

        return recruitmentService.updateRecruitment(recruitmentId, dto).apply {
            var refresh = false

            if (team.roles.contains(oldRole!!)) {
                team.roles.remove(oldRole)
                refresh = true
            }

            if (!team.roles.contains(this.role!!)) {
                team.roles.add(this.role!!)
                refresh = true
            }

            if (refresh) {
                teamRepository.save(team)
                teamCacheManager.setTeamCache(team)
            }
            esService.updateTeamDoc(team)
        }
    }

    override fun deleteTeamRecruitment(teamId: Long, recruitmentId: Long) {
        val team = getTeam(teamId, useCache = false)

        val oldRole = recruitmentService.getRecruitmentById(recruitmentId).role

        recruitmentService.deleteRecruitment(recruitmentId).apply {
            if (team.roles.contains(oldRole!!)) {
                team.roles.remove(oldRole)
                teamRepository.save(team)
                teamCacheManager.setTeamCache(team)
            }
            esService.deleteTeamDoc(teamId)
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

    override fun rebuildTeamDoc() {
        val teams = teamRepository.findAll().map { TeamDoc(it) }
        esService.rebuildIndex(teams)
    }

    override fun recommendUsers(teamId: Long, pageRequest: PageRequest): PagedList<User> {
        getTeam(teamId, useCache = true)
        val ids = recommendService.getRecommendUserIds(teamId)
        return PagedList(userRepository.findAllByIdsOrdered(ids, pageRequest)).apply {
            results.forEach { it.getInfo() }
        }
    }
}