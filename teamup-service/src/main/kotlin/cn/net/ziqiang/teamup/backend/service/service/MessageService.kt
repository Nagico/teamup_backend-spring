package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message

interface MessageService {
    fun deliverToWS(message: String) : Boolean

    fun sendMsg(message: Message)
}