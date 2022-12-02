package cn.net.ziqiang.teamup.backend.service.impl

import cn.net.ziqiang.teamup.backend.constant.status.ReportStatus
import cn.net.ziqiang.teamup.backend.constant.type.ReportType
import cn.net.ziqiang.teamup.backend.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.pojo.entity.Report
import cn.net.ziqiang.teamup.backend.pojo.entity.User
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException
import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import cn.net.ziqiang.teamup.backend.dao.repository.ReportRepository
import cn.net.ziqiang.teamup.backend.service.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class ReportServiceImpl : cn.net.ziqiang.teamup.backend.service.ReportService {
    @Autowired
    private lateinit var reportRepository: ReportRepository

    override fun createReport(createUser: User, reportUser: Report): Report {
        val report = Report(
            type = reportUser.type,
            objectId = reportUser.objectId,
            detail = reportUser.detail,
            status = ReportStatus.Pending,
            result = "",
            user = createUser,
            createTime = Date()
        )

        reportRepository.save(report)

        return report
    }

    override fun deleteReport(reportId: Long) {
        val report = getReport(reportId)
        reportRepository.delete(report)
    }

    override fun updateReport(reportId: Long, reportManager: Report): Report {
        val report = reportRepository.findById(reportId).get()

        report.status = reportManager.status
        report.result = reportManager.result
        reportRepository.save(report)

        return report
    }

    override fun queryReports(queryMap: Map<String, Any>, pageRequest: PageRequest): PagedList<Report, Report> {
        val res =  reportRepository.findAll( { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            if (queryMap.containsKey("type")) {
                predicates.add(cb.equal(root.get<ReportType>("type"), queryMap["type"]))
            }
            if (queryMap.containsKey("objectId")) {
                predicates.add(cb.equal(root.get<Long>("objectId"), queryMap["objectId"]))
            }
            if (queryMap.containsKey("status")) {
                predicates.add(cb.equal(root.get<ReportStatus>("status"), queryMap["status"]))
            }
            if (queryMap.containsKey("userId")) {
                predicates.add(cb.equal(root.get<User>("user").get<Long>("id"), queryMap["userId"]))
            }
            cb.and(*predicates.toTypedArray())
        }, pageRequest)

        return PagedList(res)
    }

    override fun getReport(reportId: Long): Report {
        return reportRepository.findById(reportId).orElseThrow { ApiException(ResultType.ResourceNotFound) }.checkPermission()
    }
}