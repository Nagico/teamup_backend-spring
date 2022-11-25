package cn.net.ziqiang.teamup.backend.service.service.impl

import cn.net.ziqiang.teamup.backend.common.constant.status.ReportStatus
import cn.net.ziqiang.teamup.backend.common.constant.type.ReportType
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType
import cn.net.ziqiang.teamup.backend.common.pojo.vo.report.ReportManagerDto
import cn.net.ziqiang.teamup.backend.common.pojo.vo.report.ReportUserDto
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Report
import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import cn.net.ziqiang.teamup.backend.common.exception.ApiException
import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.dao.repository.ReportRepository
import cn.net.ziqiang.teamup.backend.service.service.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class ReportServiceImpl : ReportService {
    @Autowired
    private lateinit var reportRepository: ReportRepository

    override fun createReport(createUser: User, reportUserDto: ReportUserDto): Report {
        val report = Report(
            type = reportUserDto.type,
            objectId = reportUserDto.objectId,
            detail = reportUserDto.detail,
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

    override fun updateReport(reportId: Long, reportManagerDto: ReportManagerDto): Report {
        val report = reportRepository.findById(reportId).get()

        report.status = reportManagerDto.status
        report.result = reportManagerDto.result
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