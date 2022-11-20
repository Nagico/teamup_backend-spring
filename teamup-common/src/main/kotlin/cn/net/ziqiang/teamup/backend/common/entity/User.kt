package cn.net.ziqiang.teamup.backend.common.entity

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.time.LocalDateTime
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
    var realName: String = "",

    @Column(name = "username", nullable = false)
    var username: String = "",

    @Column(name = "student_id")
    var studentId: String? = null,

    @Column(name = "faculty")
    var faculty: String? = null,

    @Column(name = "phone")
    var phone: String? = null,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.None,

    @Column(name = "openid", nullable = false)
    var openid: String = "",

    @Column(name = "avatar_url")
    var avatarUrl: String? = null,

    @Column(name = "blocked")
    var blocked: Boolean = false,

    @CreatedDate
    @Column(name = "create_time")
    var createTime: LocalDateTime? = null,


    ): Serializable