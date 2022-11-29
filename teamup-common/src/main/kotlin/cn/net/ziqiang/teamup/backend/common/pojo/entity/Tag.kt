package cn.net.ziqiang.teamup.backend.common.pojo.entity

import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "tag")
class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "content", nullable = false, length = 30)
    @Schema(description = "标签内容")
    var content: String? = null
}