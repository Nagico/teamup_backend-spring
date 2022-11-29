package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import org.springframework.data.domain.PageRequest

interface RecruitmentService {
    fun getRecruitmentList(pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO>

    fun getRecruitmentListByTeamId(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO>

    fun getRecruitmentById(id: Long): RecruitmentVO

    fun createRecruitment(dto: RecruitmentDto): RecruitmentVO

    fun updateRecruitment(id: Long, dto: RecruitmentDto): RecruitmentVO

    fun deleteRecruitment(id: Long)
}