package cn.net.ziqiang.teamup.backend.common.pojo.vo.message

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType
import javax.validation.constraints.NotEmpty

data class MessageVO(
    @field:NotEmpty
    var id: String? = null,

    var type: MessageType = MessageType.Chat,

    @field:NotEmpty
    var content: String = "",

    var receiver: Long? = null,
)
