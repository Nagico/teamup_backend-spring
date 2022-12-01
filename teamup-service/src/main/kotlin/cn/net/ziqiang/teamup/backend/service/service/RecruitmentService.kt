package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.recruitment.RecruitmentVO
import org.springframework.data.domain.PageRequest

interface RecruitmentService {
    /**
     * 获取招募列表
     *
     * @param pageRequest
     * @return
     */
    fun getRecruitmentList(pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO>

    /**
     * 根据队伍id获取招聘列表
     *
     * @param teamId
     * @param pageRequest
     * @return
     */
    fun getRecruitmentListByTeamId(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment, RecruitmentVO>

    /**
     * 根据id获取招募信息
     *
     * @param id
     * @return
     */
    fun getRecruitmentById(id: Long): RecruitmentVO

    /**
     * 创建招募
     *
     * @param dto
     * @return
     */
    fun createRecruitment(dto: RecruitmentDto): RecruitmentVO

    /**
     * 更新招募
     *
     * @param id
     * @param dto
     * @return
     */
    fun updateRecruitment(id: Long, dto: RecruitmentDto): RecruitmentVO

    /**
     *
     *
     * @param id
     */
    fun deleteRecruitment(id: Long) : RecruitmentVO
}