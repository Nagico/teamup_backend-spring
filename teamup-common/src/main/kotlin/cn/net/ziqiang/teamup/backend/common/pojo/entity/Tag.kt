package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "team_tag")
@Data
public class TeamTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "所属队伍")
    Team team;

    @Column(name = "content", nullable = false, length = 30)
    @Schema(description = "标签内容")
    String content;
}
