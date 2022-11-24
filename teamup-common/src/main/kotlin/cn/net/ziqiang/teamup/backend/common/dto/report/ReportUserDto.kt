package cn.net.ziqiang.teamup.backend.common.dto.report

import cn.net.ziqiang.teamup.backend.common.constant.type.ReportType
import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Schema(description = "用户创建报告Dto")
data class ReportUserDto (
    @Column(name = "type")
    @Schema(description = "类型")
    @Enumerated(EnumType.ORDINAL)
    var type: ReportType = ReportType.None,

    @Column(name = "object_id")
    @Schema(description = "对象id")
    var objectId: Long = 0,

    @Column(name = "detail")
    @Schema(description = "举报详情")
    var detail: String = "",
)