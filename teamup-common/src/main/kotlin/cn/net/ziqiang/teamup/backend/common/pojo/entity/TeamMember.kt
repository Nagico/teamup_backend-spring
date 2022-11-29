package cn.net.ziqiang.teamup.backend.common.pojo.entity

import com.vladmihalcea.hibernate.type.json.JsonStringType
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "team_member")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "team_id")
    @Schema(description = "所属队伍")
    var team: Team? = null

    @Type(type = "json")
    @Column(name = "roles", nullable = false, columnDefinition = "json")
    @Schema(description = "角色")
    var roles: MutableList<Role> = mutableListOf()

    @Column(name = "faculty")
    @Schema(description = "学院")
    var faculty: String? = null

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "成员描述")
    var description: String? = null
}