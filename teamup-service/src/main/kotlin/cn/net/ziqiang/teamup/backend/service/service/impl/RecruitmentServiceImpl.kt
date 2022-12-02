package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.dao.repository.RecruitmentRepository
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRoleRepository
import cn.net.ziqiang.teamup.backend.service.cache.RecruitmentCacheManager
import cn.net.ziqiang.teamup.backend.service.service.RecruitmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.concurrent.thread

@Service
class RecruitmentServiceImpl : RecruitmentService {
    @Autowired
    private lateinit var recruitmentRepository: RecruitmentRepository
    @Autowired
    private lateinit var teamRoleRepository: TeamRoleRepository
    @Autowired
    private lateinit var recruitmentCacheManager: RecruitmentCacheManager

    private fun getRecruitment(id: Long, useCache: Boolean = true): Recruitment {
        return if (useCache) {
            recruitmentCacheManager.getRecruitmentCache(id) ?: run {
                recruitmentRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound, "招募不存在") }.apply {
                    recruitmentCacheManager.setRecruitmentCache(this)
                }
            }
        } else {
            recruitmentRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound, "招募不存在") }
        }
    }

    override fun getRecruitmentList(pageRequest: PageRequest): PagedList<Recruitment, Recruitment> {
        val recruitmentList = recruitmentRepository.findByTeam_RecruitingTrue(pageRequest)
        return PagedList(recruitmentList) { it }
    }

    override fun getRecruitmentListByTeamId(
        teamId: Long,
        pageRequest: PageRequest
    ): PagedList<Recruitment, Recruitment> {
        val cachedList = recruitmentCacheManager.getRecruitmentListByTeamIdCache(teamId)

        return if (cachedList != null) {
            PagedList(cachedList, pageRequest) { it }
        } else {
            val recruitmentList = recruitmentRepository.findByTeamId(teamId, pageRequest)
            thread {
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
            }
            PagedList(recruitmentList) { it }
        }
    }

    override fun getRecruitmentListByTeamId(teamId: Long): List<Recruitment> {
        return recruitmentCacheManager.getRecruitmentListByTeamIdCache(teamId) ?: run {
            recruitmentRepository.findByTeamId(teamId).apply {
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, this)
            }
        }
    }

    override fun getRecruitmentById(id: Long): Recruitment {
        return getRecruitment(id)
    }

    override fun createRecruitment(dto: Recruitment): Recruitment {
        val recruitment = Recruitment(
            team = dto.team!!,
            role = teamRoleRepository.findById(dto.role!!.id!!).orElseThrow { ApiException(ResultType.ResourceNotFound, "角色不存在") },
            requirements = dto.requirements,
        )

        return recruitmentRepository.save(recruitment).apply {
            val teamId = team!!.id!!
            thread {
                recruitmentCacheManager.setRecruitmentCache(this)
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
            }
        }
    }

    override fun updateRecruitment(id: Long, dto: Recruitment): Recruitment {
        val recruitment = getRecruitment(id, false)
        dto.role?.let { recruitment.role = teamRoleRepository.findById(it.id!!).orElseThrow { ApiException(ResultType.ResourceNotFound, "角色不存在")} }
        dto.requirements.let { recruitment.requirements = it }

        return recruitmentRepository.save(recruitment).apply {
            val teamId = team!!.id!!
            thread {
                recruitmentCacheManager.setRecruitmentCache(this)
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
            }
        }
    }

    override fun deleteRecruitment(id: Long) : Recruitment {
        val teamId = getRecruitment(id, false).team!!.id!!
        val recruitment = recruitmentRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound, "招募不存在") }

        recruitmentRepository.delete(recruitment)
        recruitmentCacheManager.deleteRecruitmentCache(id)
        recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))

        return recruitment
    }
}