package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "recruitment")
@Data
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "所属队伍")
    Team team;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "角色")
    Role role;

    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL)
    @Schema(description = "需求")
    List<Requirement> requirements;
}
