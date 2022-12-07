package cn.net.ziqiang.teamup.backend.pojo.entity

import cn.net.ziqiang.teamup.backend.constant.UserRole
import cn.net.ziqiang.teamup.backend.constant.status.UserStatus
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vladmihalcea.hibernate.type.json.JsonStringType
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.security.Principal
import java.util.*
import javax.persistence.*

@Entity(name = "user")
@TypeDef(name = "json", typeClass = JsonStringType::class)
@JsonIgnoreProperties(ignoreUnknown = true, value = ["password"], allowSetters = true)
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

    @Column(name = "password", nullable = false)
    @Schema(description = "密码")
    var password: String = "",

    @Column(name = "student_id")
    @Schema(description = "学号")
    var studentId: String? = null,

    @Column(name = "faculty")
    @Schema(description = "学院")
    var faculty: String? = null,

    @Column(name = "grade")
    @Schema(description = "年级")
    var grade: String? = null,

    @Column(name = "phone", nullable = false)
    @Schema(description = "手机")
    var phone: String = "",

    @Column(name = "introduction")
    @Schema(description = "个人介绍")
    var introduction: String? = null,

    @Type(type = "json")
    @Column(name = "awards", nullable = false, columnDefinition = "json")
    @Schema(description = "获奖情况")
    var awards: List<String>? = null,

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

    @Column(name = "last_login")
    @Schema(description = "最后登录时间")
    var lastLogin: Date? = null,

    @Transient
    @Schema(description = "当前状态")
    var status: UserStatus? = null,

    @Transient
    @Schema(description = "队伍数量")
    var teamCount: Long? = null,
): Serializable, Principal {
    override fun toString(): String {
        return "User(id=$id, username='$username', openid='$openid')"
    }

    /**
     * 重写Principal的getName方法 ws鉴权使用
     *
     * @return
     */
    override fun getName(): String {
        return id.toString()
    }
}