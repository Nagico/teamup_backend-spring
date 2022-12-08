package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.dao.repository.CompetitionRepository
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRoleRepository
import cn.net.ziqiang.teamup.backend.dao.repository.UserRepository
import cn.net.ziqiang.teamup.backend.pojo.entity.Competition
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
}