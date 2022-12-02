package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.pojo.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TagRepository : JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    fun findByContent(content: String): Tag?

    fun findAllByContentIn(content: List<String>): List<Tag>

}