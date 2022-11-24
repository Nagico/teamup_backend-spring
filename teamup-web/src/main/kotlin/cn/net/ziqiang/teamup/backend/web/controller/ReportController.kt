package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import cn.net.ziqiang.teamup.backend.common.constant.status.ReportStatus
import cn.net.ziqiang.teamup.backend.common.constant.type.ReportType
import cn.net.ziqiang.teamup.backend.common.dto.report.ReportManagerDto
import cn.net.ziqiang.teamup.backend.common.dto.report.ReportUserDto
import cn.net.ziqiang.teamup.backend.common.entity.Report
import cn.net.ziqiang.teamup.backend.common.pagenation.PagedList
import cn.net.ziqiang.teamup.backend.common.utils.sort.handleSort
import cn.net.ziqiang.teamup.backend.service.service.report.ReportService
import cn.net.ziqiang.teamup.backend.web.annotation.permission.Owner
import cn.net.ziqiang.teamup.backend.web.annotation.permission.OwnerOrManager
import cn.net.ziqiang.teamup.backend.web.annotation.role.AllowRole
import cn.net.ziqiang.teamup.backend.web.annotation.user.ActiveUser
import cn.net.ziqiang.teamup.backend.web.security.SecurityContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reports")
class ReportController {
    @Autowired
    private lateinit var reportService: ReportService

    @ActiveUser
    @GetMapping
    fun getReports(
        name: String?,
        @RequestParam userId: Long?,
        @RequestParam type: ReportType?,
        @RequestParam objectId: Long?,
        @RequestParam status: ReportStatus?,
        @RequestParam(defaultValue = "-id") order: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
    ) : PagedList<Report, Report> {
        val user = SecurityContextUtils.user

        var searchUserId = userId
        if (user.role != UserRole.Manager) {  // 只有管理员能查看所有的举报
            searchUserId = user.id!!
        }

        val pageRequest = PageRequest.of(page - 1, pageSize, handleSort(order))
        val queryMap = mutableMapOf<String, Any>()
        if (searchUserId != null) {
            queryMap["userId"] = searchUserId
        }
        if (type != null) {
            queryMap["type"] = type
        }
        if (objectId != null) {
            queryMap["objectId"] = objectId
        }
        if (status != null) {
            queryMap["status"] = status
        }
        return reportService.queryReports(queryMap, pageRequest)
    }

    @ActiveUser
    @OwnerOrManager(field = "report")
    @GetMapping("/{id}")
    fun getReport(@PathVariable id: Long): Report {
        return reportService.getReport(id)
    }

    @ActiveUser
    @PostMapping
    fun addReport(@RequestBody report: ReportUserDto) : Report {
        val user = SecurityContextUtils.user
        return reportService.createReport(user, report)
    }

    @ActiveUser
    @AllowRole(UserRole.Manager)
    @PutMapping("/{id}")
    fun updateReport(@PathVariable id: Long, @RequestBody report: ReportManagerDto) : Report {
        return reportService.updateReport(id, report)
    }

    @ActiveUser
    @Owner(field = "report")
    @DeleteMapping("/{id}")
    fun deleteReport(@PathVariable id: Long) {
        reportService.deleteReport(id)
    }
}