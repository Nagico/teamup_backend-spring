package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
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

    override fun getRecruitmentList(pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO> {
        val recruitmentList = recruitmentRepository.findByTeam_RecruitingTrue(pageRequest)
        return PagedList(recruitmentList) { RecruitmentVO(it) }
    }

    override fun getRecruitmentListByTeamId(
        teamId: Long,
        pageRequest: PageRequest
    ): PagedList<Recruitment, RecruitmentVO> {
        val cachedList = recruitmentCacheManager.getRecruitmentListByTeamIdCache(teamId)

        return if (cachedList != null) {
            PagedList(cachedList, pageRequest) { RecruitmentVO(it) }
        } else {
            val recruitmentList = recruitmentRepository.findByTeamId(teamId, pageRequest)
            thread {
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
            }
            PagedList(recruitmentList) { RecruitmentVO(it) }
        }
    }

    override fun getRecruitmentById(id: Long): RecruitmentVO {
        return RecruitmentVO(getRecruitment(id))
    }

    override fun createRecruitment(dto: RecruitmentDto): RecruitmentVO {
        val recruitment = Recruitment(
            team = dto.team!!,
            role = teamRoleRepository.findById(dto.role!!).orElseThrow { ApiException(ResultType.ResourceNotFound, "角色不存在") },
            requirements = dto.requirements!! as MutableList<String>,
        )

        return RecruitmentVO(recruitmentRepository.save(recruitment).apply {
            val teamId = team!!.id!!
            thread {
                recruitmentCacheManager.setRecruitmentCache(this)
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
            }
        })
    }

    override fun updateRecruitment(id: Long, dto: RecruitmentDto): RecruitmentVO {
        val recruitment = getRecruitment(id, false)
        dto.role?.let { recruitment.role = teamRoleRepository.findById(it).orElseThrow { ApiException(ResultType.ResourceNotFound, "角色不存在")} }
        dto.requirements?.let { recruitment.requirements = it as MutableList<String> }

        return RecruitmentVO(recruitmentRepository.save(recruitment).apply {
            val teamId = team!!.id!!
            thread {
                recruitmentCacheManager.setRecruitmentCache(this)
                recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
            }
        })
    }

    override fun deleteRecruitment(id: Long) : RecruitmentVO {
        val teamId = getRecruitment(id, false).team!!.id!!
        val recruitment = recruitmentRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound, "招募不存在") }
        thread {
            recruitmentRepository.deleteById(id)
            recruitmentCacheManager.deleteRecruitmentCache(id)
            recruitmentCacheManager.setRecruitmentListByTeamIdCache(teamId, recruitmentRepository.findByTeamId(teamId))
        }
        return RecruitmentVO(recruitment)
    }
}