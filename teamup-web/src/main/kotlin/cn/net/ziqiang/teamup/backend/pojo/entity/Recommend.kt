package cn.net.ziqiang.teamup.backend.pojo.entity

import cn.net.ziqiang.teamup.backend.constant.type.RecommendType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vladmihalcea.hibernate.type.json.JsonStringType
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "recommend")
@TypeDef(name = "json", typeClass = JsonStringType::class)
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
class Recommend (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    @Schema(description = "类型")
    var type: RecommendType? = null,

    @Column(name = "object_id")
    @Schema(description = "对象ID")
    var objectId: Long? = null,

    @Type(type = "json")
    @Column(name = "items", nullable = false, columnDefinition = "json")
    @Schema(description = "推荐ID")
    var items: List<Long> = listOf(),
)