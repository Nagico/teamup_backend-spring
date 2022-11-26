package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "competition")
@Data
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "名称")
    String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "描述")
    String description = "";

    @Column(name = "verified", nullable = false)
    @Schema(description = "是否通过审核")
    Boolean verified = false;

    @Column(name = "logo", nullable = false)
    @Schema(description = "logo")
    String logo = "";

    @Column(name = "start_time")
    @Schema(description = "开始时间")
    Date startTime;

    @Column(name = "end_time")
    @Schema(description = "结束时间")
    Date endTime;

    @Column(name = "finish")
    @Schema(description = "是否结束")
    Boolean finish;

    @Column(name = "score")
    @Schema(description = "比赛评分")
    Integer score;

    @Column(name = "team_count")
    @Schema(description = "队伍数量")
    Integer teamCount;
}
