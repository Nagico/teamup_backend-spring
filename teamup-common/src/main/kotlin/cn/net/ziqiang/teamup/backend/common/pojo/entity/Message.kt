package cn.net.ziqiang.teamup.backend.common.pojo.entity

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType

data class Message (
    var type: MessageType,
    var content: String,
    var sender: String,
    var receiver: String? = null,
)