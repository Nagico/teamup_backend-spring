package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "team")
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "名称")
    String name;

    @OneToOne
    @JoinColumn(name = "competition_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "竞赛")
    Competition competition;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false, referencedColumnName = "id")
    @Schema(description = "队长")
    User leader;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "队伍描述")
    String description = "";

    @Column(name = "like_count", nullable = false)
    @Schema(description = "点赞数")
    Long likeCount = 0L;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    @Schema(description = "队伍成员")
    List<TeamMember> members;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    @Schema(description = "招募信息")
    List<Recruitment> recruitments;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    @Schema(description = "标签")
    List<TeamTag> tags;

    @Column(name = "recruiting", nullable = false)
    @Schema(description = "是否招募中")
    Boolean recruiting = true;
}
