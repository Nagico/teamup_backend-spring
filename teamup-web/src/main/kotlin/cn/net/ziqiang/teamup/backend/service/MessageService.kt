package cn.net.ziqiang.teamup.backend.service

import cn.net.ziqiang.teamup.backend.pojo.entity.Message

interface MessageService {
    /**
     * 传送消息
     *
     * @param message
     */
    fun deliver(message: cn.net.ziqiang.teamup.backend.pojo.entity.Message)

    /**
     * 传送消息
     *
     * @param message
     * @return
     */
    fun deliver(message: String) : Boolean

    /**
     * 客户端发送消息
     *
     * @param senderId
     * @param message
     */
    fun sendMsg(senderId: Long, message: cn.net.ziqiang.teamup.backend.pojo.entity.Message)

    /**
     * 获取离线消息列表
     *
     * @param receiver
     * @return
     */
    fun getOfflineMsg(receiver: Long): List<cn.net.ziqiang.teamup.backend.pojo.entity.Message>
}