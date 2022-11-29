package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import cn.net.ziqiang.teamup.backend.dao.repository.RecruitmentRepository
import cn.net.ziqiang.teamup.backend.dao.repository.RoleRepository
import cn.net.ziqiang.teamup.backend.service.service.RecruitmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class RecruitmentServiceImpl : RecruitmentService {
    @Autowired
    private lateinit var recruitmentRepository: RecruitmentRepository
    @Autowired
    private lateinit var roleRepository: RoleRepository

    override fun getRecruitmentList(pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO> {
        val recruitmentList = recruitmentRepository.findByTeam_RecruitingTrue(pageRequest)
        return PagedList(recruitmentList) { RecruitmentVO(it) }
    }

    override fun getRecruitmentListByTeamId(
        teamId: Long,
        pageRequest: PageRequest,
    ): PagedList<Recruitment, RecruitmentVO> {
        val recruitmentList = recruitmentRepository.findByTeamId(teamId, pageRequest)
        return PagedList(recruitmentList) { RecruitmentVO(it) }
    }

    override fun getRecruitmentById(id: Long): RecruitmentVO {
        return RecruitmentVO(recruitmentRepository.getById(id))
    }

    override fun createRecruitment(dto: RecruitmentDto): RecruitmentVO {
        val recruitment = Recruitment(
            team = dto.team!!,
            role = roleRepository.findById(dto.role!!).orElseThrow { ApiException(ResultType.ResourceNotFound, "角色不存在") },
            requirements = dto.requirements!! as MutableList<String>,
        )

        return RecruitmentVO(recruitmentRepository.save(recruitment))
    }

    override fun updateRecruitment(id: Long, dto: RecruitmentDto): RecruitmentVO {
        val recruitment = recruitmentRepository.getById(id)
        dto.role?.let { recruitment.role = roleRepository.findById(it).orElseThrow { ApiException(ResultType.ResourceNotFound, "角色不存在")} }
        dto.requirements?.let { recruitment.requirements = it as MutableList<String> }

        return RecruitmentVO(recruitmentRepository.save(recruitment))
    }

    override fun deleteRecruitment(id: Long) {
        val recruitment = recruitmentRepository.findById(id).orElseThrow { ApiException(ResultType.ResourceNotFound, "招募不存在") }
        recruitmentRepository.delete(recruitment)
    }
}