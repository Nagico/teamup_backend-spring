package cn.net.ziqiang.teamup.backend.pojo.entity

import cn.net.ziqiang.teamup.backend.constant.status.ReportStatus
import cn.net.ziqiang.teamup.backend.constant.type.ReportType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.Date
import javax.persistence.*

@Entity(name = "report")
class Report(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

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

    @Column(name = "status")
    @Schema(description = "举报状态")
    @Enumerated(EnumType.ORDINAL)
    var status: ReportStatus = ReportStatus.None,

    @Column(name = "result")
    @Schema(description = "举报结果")
    var result: String = "",

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.REFRESH], optional = false)
    @JoinColumn(name = "user_id")
    @Schema(description = "用户")
    var user: User? = null,

    @Column(name = "create_time")
    var createTime: Date? = null,
): cn.net.ziqiang.teamup.backend.pojo.entity.PermissionChecker<Report>("report")
