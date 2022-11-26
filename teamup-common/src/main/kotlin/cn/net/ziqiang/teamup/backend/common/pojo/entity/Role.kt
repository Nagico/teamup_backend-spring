package cn.net.ziqiang.teamup.backend.common.pojo.entity

import javax.persistence.*

@Entity(name = "role")
class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "level")
    var level: Int? = null,

    @Column(name = "pid")
    var pid: Long? = null,
)