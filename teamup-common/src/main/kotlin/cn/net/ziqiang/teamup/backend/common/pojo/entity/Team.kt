package cn.net.ziqiang.teamup.backend.common.pojo.entity

import com.vladmihalcea.hibernate.type.json.JsonStringType
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "team")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class Team (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "名称")
    var name: String? = null,

    @OneToOne
    @JoinColumn(name = "competition_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "竞赛")
    var competition: Competition? = null,

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "队长")
    var leader: User? = null,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "队伍描述")
    var description: String? = null,

    @Column(name = "like_count", nullable = false)
    @Schema(description = "点赞数")
    var likeCount: Long = 0,

    @Type(type = "json")
    @Column(name = "members", nullable = false, columnDefinition = "json")
    @Schema(description = "队伍成员")
    var members: MutableList<TeamMember> = mutableListOf(),

    @Type(type = "json")
    @Column(name = "tags", nullable = false, columnDefinition = "json")
    @Schema(description = "标签")
    var tags: MutableList<Tag> = mutableListOf(),

    @Column(name = "recruiting", nullable = false)
    @Schema(description = "是否招募中")
    var recruiting: Boolean = false,
): PermissionChecker<Team>("team", "leader") {
    override fun toString(): String {
        return "Team(id=$id, name=$name)"
    }

}