package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "team_member")
@Data
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @Schema(description = "所属队伍")
    Team team;

    @ManyToMany
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "角色")
    List<Role> roles;

    @Column(name = "faculty")
    @Schema(description = "学院")
    String faculty;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "成员描述")
    String description = "";
}
