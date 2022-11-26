package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Entity(name = "requirement")
@Data
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    @Schema(description = "所属招募")
    Recruitment recruitment;

    @Column(name = "content", nullable = false)
    @Schema(description = "需求内容")
    String content;
}
