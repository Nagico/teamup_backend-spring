package cn.net.ziqiang.teamup.backend.pojo.entity

import cn.net.ziqiang.teamup.backend.pojo.entity.PermissionChecker
import cn.net.ziqiang.teamup.backend.constant.type.FileType
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*
import javax.persistence.*

@Entity(name = "file")
class File (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.REFRESH], optional = false)
    @JoinColumn(name = "user_id")
    @Schema(description = "用户")
    var user: cn.net.ziqiang.teamup.backend.pojo.entity.User? = null,

    @Column(name = "name")
    @Schema(description = "文件名")
    var name: String = "",

    @Column(name = "type")
    @Schema(defaultValue = "文件类型")
    @Enumerated(EnumType.STRING)
    var type: FileType = FileType.None,

    @Column(name = "url")
    @Schema(description = "文件地址")
    var url: String = "",

    @Column(name = "size")
    @Schema(description = "文件大小")
    var size: Long = 0,

    @Column(name = "expired")
    @Schema(description = "是否过期")
    var expired: Boolean = false,

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    var createTime: Date? = null,
): cn.net.ziqiang.teamup.backend.pojo.entity.PermissionChecker<cn.net.ziqiang.teamup.backend.pojo.entity.File>("file") {
    override fun toString(): String {
        return "File(id=$id, name='$name')"
    }
}