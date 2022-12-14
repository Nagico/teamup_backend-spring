package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
    fun findByOpenid(openid: String): User?

    fun countByUsername(username: String): Int

    fun countByPhone(phone: String): Int

    fun findByUsername(username: String): User?

    fun findByPhone(phone: String): User?

    @Query("select u from user u where u.id in :ids order by FIELD(u.id, :ids)")
    fun findAllByIdsOrdered(ids: List<Long>, pageable: Pageable): Page<User>
}