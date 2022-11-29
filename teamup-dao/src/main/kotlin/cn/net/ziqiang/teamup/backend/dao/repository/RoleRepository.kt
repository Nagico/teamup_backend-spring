package cn.net.ziqiang.teamup.backend.dao.repository

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RoleRepository : JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

}