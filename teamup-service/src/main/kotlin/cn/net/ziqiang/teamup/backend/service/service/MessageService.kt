package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.vo.message.MessageVO

interface MessageService {
    fun deliverToUser(message: String) : Boolean

    fun sendMsg(senderId: Long, vo: MessageVO)
}