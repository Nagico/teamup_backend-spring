package cn.net.ziqiang.teamup.backend.service.service

import cn.net.ziqiang.teamup.backend.common.pagination.PagedList
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message
import cn.net.ziqiang.teamup.backend.common.pojo.vo.message.MessageVO
import org.springframework.data.domain.PageRequest

interface MessageService {
    /**
     * 传送消息
     *
     * @param message
     */
    fun deliver(message: Message)

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
     * @param vo
     */
    fun sendMsg(senderId: Long, vo: MessageVO)

    /**
     * 获取离线消息列表
     *
     * @param receiver
     * @return
     */
    fun getOfflineMsg(receiver: Long): List<Message>
}