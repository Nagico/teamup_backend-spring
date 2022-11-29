package cn.net.ziqiang.teamup.backend.common.pojo.entity

import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamRoleVO
import com.vladmihalcea.hibernate.type.json.JsonStringType
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef

@TypeDef(name = "json", typeClass = JsonStringType::class)
class TeamMember (
    @Type(type = "json")
    @Schema(description = "角色")
    var roles: MutableList<TeamRoleVO> = mutableListOf(),

    @Schema(description = "学院")
    var faculty: String? = null,

    @Schema(description = "成员描述")
    var description: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TeamMember) return false

        if (roles != other.roles) return false
        if (faculty != other.faculty) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roles.hashCode()
        result = 31 * result + (faculty?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}