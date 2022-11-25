package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.common.pojo.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByOpenid(openid: String): User?

    fun countByUsername(username: String): Int

    fun countByPhone(phone: String): Int

    fun findByUsername(username: String): User?

    fun findByPhone(phone: String): User?
}