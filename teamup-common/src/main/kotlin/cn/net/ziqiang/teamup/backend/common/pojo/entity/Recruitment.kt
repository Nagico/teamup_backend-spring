package cn.net.ziqiang.teamup.backend.common.pojo.entity

import com.vladmihalcea.hibernate.type.json.JsonStringType
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "recruitment")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "所属队伍")
    var team: Team? = null

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "角色")
    var role: Role? = null

    @Type(type = "json")
    @Column(name = "requirements", nullable = false, columnDefinition = "json")
    @Schema(description = "需求")
    var requirements: MutableList<String> = mutableListOf()
}