package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.Report
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import org.springframework.data.domain.PageRequest

interface ReportService {

    fun createReport(createUser: User, reportUser: Report) : Report

    fun deleteReport(reportId: Long)

    fun updateReport(reportId: Long, reportManager: Report) : Report

    fun queryReports(queryMap: Map<String, Any>, pageRequest: PageRequest) : PagedList<Report>

    fun getReport(reportId: Long) : Report
}