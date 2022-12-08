package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.dao.repository.CompetitionRepository
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRepository
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRoleRepository
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.pojo.entity.Competition
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.pojo.entity.TeamRole
import cn.net.ziqiang.teamup.backend.service.RecommendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecommendServiceImpl : RecommendService {
    @Autowired
    private lateinit var competitionRepository: CompetitionRepository
    @Autowired
    private lateinit var teamRoleRepository: TeamRoleRepository
    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun getUserSubscribeCompetition(userId: Long): List<Competition> {
        val user = userRepository.getById(userId)
        return competitionRepository.findAllByIdIn(user.subscribeCompetition ?: emptySet())
    }

    override fun addUserSubscribeCompetition(userId: Long, competitionId: Long) {
        val user = userRepository.getById(userId)
        user.subscribeCompetition = user.subscribeCompetition?.plus(competitionId) ?: setOf(competitionId)

        userRepository.save(user)
    }

    override fun deleteUserSubscribeCompetition(userId: Long, competitionId: Long) {
        val user = userRepository.getById(userId)
        user.subscribeCompetition = user.subscribeCompetition?.minus(competitionId) ?: emptySet()

        userRepository.save(user)
    }

    override fun checkUserSubscribeCompetition(userId: Long, competitionId: Long): Boolean {
        val user = userRepository.getById(userId)
        return user.subscribeCompetition?.contains(competitionId) ?: false
    }

    override fun getUserSubscribeRole(userId: Long): List<TeamRole> {
        val user = userRepository.getById(userId)
        return teamRoleRepository.getAllByIdIn(user.subscribeRole ?: emptySet())
    }

    override fun addUserSubscribeRole(userId: Long, roleId: Long) {
        val user = userRepository.getById(userId)
        user.subscribeRole = user.subscribeRole?.plus(roleId) ?: setOf(roleId)

        userRepository.save(user)
    }

    override fun deleteUserSubscribeRole(userId: Long, roleId: Long) {
        val user = userRepository.getById(userId)
        user.subscribeRole = user.subscribeRole?.minus(roleId) ?: emptySet()

        userRepository.save(user)
    }

    override fun getUserFavoriteTeam(userId: Long): Set<Team> {
        val user = userRepository.getById(userId)
        return teamRepository.findAllByIdIn(user.favoriteTeam ?: emptySet()).onEach { it.favorite = true }
    }

    override fun addUserFavoriteTeam(userId: Long, teamId: Long) {
        val user = userRepository.getById(userId)
        user.favoriteTeam = user.favoriteTeam?.plus(teamId) ?: setOf(teamId)

        userRepository.save(user)
    }

    override fun deleteUserFavoriteTeam(userId: Long, teamId: Long) {
        val user = userRepository.getById(userId)
        user.favoriteTeam = user.favoriteTeam?.minus(teamId) ?: emptySet()

        userRepository.save(user)
    }

    override fun checkUserTeam(userId: Long, team: Team) {
        val user = userRepository.getById(userId)
        team.favorite = user.favoriteTeam?.contains(team.id) ?: false
        team.interested = user.interestingTeam?.contains(team.id) ?: false
        team.uninterested = user.uninterestingTeam?.contains(team.id) ?: false
    }

    override fun checkUserFavoriteTeams(userId: Long, teams: List<Team>) {
        val user = userRepository.getById(userId)
        teams.forEach {
            it.favorite = user.favoriteTeam?.contains(it.id) ?: false
        }
    }

    override fun addUserInterestingTeam(userId: Long, teamId: Long) {
        val user = userRepository.getById(userId)
        user.interestingTeam = user.interestingTeam?.plus(teamId) ?: setOf(teamId)
        user.uninterestingTeam = user.uninterestingTeam?.minus(teamId) ?: emptySet()

        userRepository.save(user)
    }

    override fun deleteUserInterestingTeam(userId: Long, teamId: Long) {
        val user = userRepository.getById(userId)
        user.interestingTeam = user.interestingTeam?.minus(teamId) ?: emptySet()

        userRepository.save(user)
    }

    override fun addUserUninterestingTeam(userId: Long, teamId: Long) {
        val user = userRepository.getById(userId)
        user.uninterestingTeam = user.uninterestingTeam?.plus(teamId) ?: setOf(teamId)
        user.interestingTeam = user.interestingTeam?.minus(teamId) ?: emptySet()

        userRepository.save(user)
    }

    override fun deleteUserUninterestingTeam(userId: Long, teamId: Long) {
        val user = userRepository.getById(userId)
        user.uninterestingTeam = user.uninterestingTeam?.minus(teamId) ?: emptySet()

        userRepository.save(user)
    }
}