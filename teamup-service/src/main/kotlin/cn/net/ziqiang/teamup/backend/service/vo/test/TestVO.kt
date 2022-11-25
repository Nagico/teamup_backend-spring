package cn.net.ziqiang.teamup.backend.service.vo.test

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.Date

@Schema(description = "测试VO")
data class TestVO(
    @Schema(description = "用户ID")
    var userId: Long?,
    @Schema(description = "用户名")
    var username: String?,
    @Schema(description = "用户角色")
    var role: UserRole,
    @Schema(description = "服务器当前时间")
    var time: Date = Date()
)