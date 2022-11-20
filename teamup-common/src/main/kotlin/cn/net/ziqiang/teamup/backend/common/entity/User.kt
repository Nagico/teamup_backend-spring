package cn.net.ziqiang.teamup.backend.common.entity

import cn.net.ziqiang.teamup.backend.common.constant.UserRole
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.persistence.Transient

@Entity(name = "user")
@JsonIgnoreProperties(value= ["handler"])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var userId: Long? = null,

    @Column(name = "real_name", nullable = false)
    var realName: String = "",

    @Column(name = "nickname", nullable = false)
    var nickname: String = "",

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

    @CreatedDate
    @Column(name = "create_time")
    var createTime: Date? = null,


    ): Serializable