package cn.net.ziqiang.teamup.backend.common.pojo.vo.message

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType

data class MessageVO(
    var id: String? = null,

    var type: MessageType = MessageType.Chat,

    var content: String = "",

    var receiver: Long? = null,
)
