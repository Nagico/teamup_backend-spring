package cn.net.ziqiang.teamup.backend.common.pojo.entity

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity(name = "user")
@JsonIgnoreProperties(value= ["handler"])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "real_name", nullable = false)
    @Schema(description = "姓名")
    var realName: String = "",

    @Column(name = "username", nullable = false)
    @Schema(description = "用户名")
    var username: String = "",

    @Column(name = "student_id")
    @Schema(description = "学号")
    var studentId: String? = null,

    @Column(name = "faculty")
    @Schema(description = "学院")
    var faculty: String? = null,

    @Column(name = "grade")
    @Schema(description = "年级")
    var grade: String? = null,

    @Column(name = "phone")
    @Schema(description = "手机")
    var phone: String? = null,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "角色")
    var role: UserRole = UserRole.User,

    @Column(name = "openid", nullable = false)
    @Schema(description = "微信openid")
    var openid: String = "",

    @Column(name = "avatar")
    @Schema(description = "头像")
    var avatar: String? = null,

    @Column(name = "active")
    @Schema(description = "是否激活")
    var active: Boolean = false,

    @Column(name = "blocked")
    @Schema(description = "是否封禁")
    var blocked: Boolean = false,

    @CreatedDate
    @Column(name = "create_time")
    @Schema(description = "创建时间")
    var createTime: Date? = null,

//    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    @Schema(description = "文件")
//    var files: MutableList<File> = mutableListOf(),
): Serializable {
    override fun toString(): String {
        return "User(id=$id, username='$username', openid='$openid')"
    }
}