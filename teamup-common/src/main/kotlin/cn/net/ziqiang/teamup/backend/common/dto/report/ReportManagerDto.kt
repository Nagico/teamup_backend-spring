package cn.net.ziqiang.teamup.backend.common.dto.report

import cn.net.ziqiang.teamup.backend.common.constant.status.ReportStatus
import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Schema(description = "管理员更新报告Dto")
data class ReportManagerDto (
    @Column(name = "status")
    @Schema(description = "举报状态")
    @Enumerated(EnumType.ORDINAL)
    var status: ReportStatus = ReportStatus.None,

    @Column(name = "result")
    @Schema(description = "举报结果")
    var result: String = "",
)