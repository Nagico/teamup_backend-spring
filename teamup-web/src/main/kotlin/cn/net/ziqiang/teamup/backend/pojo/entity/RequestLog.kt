package cn.net.ziqiang.teamup.backend.pojo.entity


import io.swagger.v3.oas.annotations.media.Schema
import java.util.Date
import javax.persistence.*

@Entity(name = "request_log")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "success", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
class RequestLog (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "controller_method")
    @Schema(description = "controller名")
    var controllerMethod: String? = null,

    @Column(name = "path")
    @Schema(description = "访问路径")
    var path: String? = null,

    @Column(name = "method")
    @Schema(description = "访问方法")
    var method: String? = null,

    @Column(name = "ip")
    var ip: String? = null,

    @Lob
    @Column(name = "header")
    var header: String? = null,

    @Lob
    @Column(name = "body")
    var body: String? = null,

    @Column(name = "time")
    @Schema(description = "耗时")
    var time: Double? = null,

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    var createTime: Date? = null,
) {
    override fun toString(): String {
        return "RequestLog{" +
                "id=" + id +
                ", path=" + path +
                ", method=" + method +
                ", ip=" + ip +
                ", header=" + header +
                ", time=" + time +
                ", createTime=" + createTime +
                "}"
    }
}
