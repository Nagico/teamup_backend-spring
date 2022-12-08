package cn.net.ziqiang.teamup.backend.pojo.vo

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "日期统计VO")
data class DateCountVO(
    @Schema(description = "日期")
    var date: String,
    @Schema(description = "数量")
    var count: Long
)
