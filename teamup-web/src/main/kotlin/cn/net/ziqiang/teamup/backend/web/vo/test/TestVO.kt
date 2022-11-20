package cn.net.ziqiang.teamup.backend.web.vo.test

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import java.time.LocalDateTime

data class TestVO(
    var userId: Long?,
    var username: String?,
    var role: UserRole,
    var time: LocalDateTime?
)