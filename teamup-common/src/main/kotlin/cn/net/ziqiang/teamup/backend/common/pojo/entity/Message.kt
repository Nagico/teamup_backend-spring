package cn.net.ziqiang.teamup.backend.common.pojo.entity

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType
import java.util.Date
import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "message", indexes = [Index(name = "idx_online_message_receiver", columnList = "receiver")])
class Message (
    @Id
    @Column(name = "id", length = 32)
    @field:NotEmpty
    var id: String? = null,

    var type: MessageType = MessageType.Chat,

    @field:NotEmpty
    var content: String = "",

    var sender: Long = 0,

    var receiver: Long? = null,

    var createTime: Date = Date(),
)