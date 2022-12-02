package cn.net.ziqiang.teamup.backend.pojo.entity

import javax.persistence.*

@Entity(name = "team_role")
class TeamRole (
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