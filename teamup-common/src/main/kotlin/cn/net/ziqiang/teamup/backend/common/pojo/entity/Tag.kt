package cn.net.ziqiang.teamup.backend.common.pojo.entity

import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.*

@Entity
@Table(name = "tag")
class Tag (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "content", nullable = false, length = 30)
    @Schema(description = "标签内容")
    var content: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tag) return false

        if (id != other.id) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }
}