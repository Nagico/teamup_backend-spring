package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface MessageRepository : JpaRepository<cn.net.ziqiang.teamup.backend.pojo.entity.Message, Long>, JpaSpecificationExecutor<cn.net.ziqiang.teamup.backend.pojo.entity.Message> {
    fun findAllByReceiverOrderByCreateTime(receiver: Long): List<cn.net.ziqiang.teamup.backend.pojo.entity.Message>

    fun deleteAllByReceiver(receiver: Long)
}