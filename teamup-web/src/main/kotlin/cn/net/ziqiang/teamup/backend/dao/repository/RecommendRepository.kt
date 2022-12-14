package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.constant.type.RecommendType
import cn.net.ziqiang.teamup.backend.pojo.entity.Recommend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RecommendRepository : JpaRepository<Recommend, Long>, JpaSpecificationExecutor<Recommend> {
    fun findByObjectIdAndType(objectId: Long, type: RecommendType): Recommend?
}