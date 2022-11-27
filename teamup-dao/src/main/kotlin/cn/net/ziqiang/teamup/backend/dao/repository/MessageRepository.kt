package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface MessageRepository : JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    fun findAllByReceiverOrderByCreateTime(receiver: Long): List<Message>

    fun deleteAllByReceiver(receiver: Long)
}