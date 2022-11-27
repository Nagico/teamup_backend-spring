package cn.net.ziqiang.teamup.backend.common.pojo.entity

import cn.net.ziqiang.teamup.backend.common.constant.type.MessageType
import org.hibernate.annotations.GenericGenerator
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "message", indexes = [Index(name = "idx_online_message_receiver", columnList = "receiver")])
class Message (
    @Id
    @Column(name = "id", length = 32)
    var id: String? = null,

    var type: MessageType = MessageType.Chat,

    var content: String = "",

    var sender: Long = 0,

    var receiver: Long? = null,

    var createTime: Date = Date(),
)