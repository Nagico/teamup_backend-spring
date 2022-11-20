package cn.net.ziqiang.teamup.backend.service.vo.test

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import java.time.LocalDateTime
import java.util.Date

data class TestVO(
    var userId: Long?,
    var username: String?,
    var role: UserRole,
    var time: Date = Date()
)