package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message

interface MessageService {
    fun deliverToWs(message: String) : Boolean

    fun deliverToWs(message: Message) : Boolean

    fun sendMsg(message: Message)
}