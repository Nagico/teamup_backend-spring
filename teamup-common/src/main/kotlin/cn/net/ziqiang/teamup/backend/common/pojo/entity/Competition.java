package cn.net.ziqiang.teamup.backend.common.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "competition")
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

    @Column(name = "finish", nullable = false)
    @Schema(description = "是否结束")
    Boolean finish = false;

    @Column(name = "score")
    @Schema(description = "比赛评分")
    Integer score;

    @Column(name = "team_count", nullable = false)
    @Schema(description = "队伍数量")
    Integer teamCount = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competition that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
