package cn.net.ziqiang.teamup.backend.service.service.report

import cn.net.ziqiang.teamup.backend.common.dto.report.ReportManagerDto
import cn.net.ziqiang.teamup.backend.common.dto.report.ReportUserDto
import cn.net.ziqiang.teamup.backend.common.entity.Report
import cn.net.ziqiang.teamup.backend.common.entity.User
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import org.springframework.data.domain.PageRequest

interface ReportService {

    fun createReport(createUser: User, reportUserDto: ReportUserDto) : Report

    fun deleteReport(reportId: Long)

    fun updateReport(reportId: Long, reportManagerDto: ReportManagerDto) : Report

    fun queryReports(queryMap: Map<String, Any>, pageRequest: PageRequest) : PagedList<Report, Report>

    fun getReport(reportId: Long) : Report
}