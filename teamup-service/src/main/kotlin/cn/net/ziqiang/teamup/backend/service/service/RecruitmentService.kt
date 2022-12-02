package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Recruitment
import org.springframework.data.domain.PageRequest

interface RecruitmentService {
    /**
     * 获取招募列表
     *
     * @param pageRequest
     * @return
     */
    fun getRecruitmentList(pageRequest: PageRequest): PagedList<Recruitment, Recruitment>

    /**
     * 根据队伍id获取招聘列表
     *
     * @param teamId
     * @param pageRequest
     * @return
     */
    fun getRecruitmentListByTeamId(teamId: Long, pageRequest: PageRequest): PagedList<Recruitment, Recruitment>

    fun getRecruitmentListByTeamId(teamId: Long): List<Recruitment>

    /**
     * 根据id获取招募信息
     *
     * @param id
     * @return
     */
    fun getRecruitmentById(id: Long): Recruitment

    /**
     * 创建招募
     *
     * @param dto
     * @return
     */
    fun createRecruitment(dto: Recruitment): Recruitment

    /**
     * 更新招募
     *
     * @param id
     * @param dto
     * @return
     */
    fun updateRecruitment(id: Long, dto: Recruitment): Recruitment

    /**
     *
     *
     * @param id
     */
    fun deleteRecruitment(id: Long) : Recruitment
}