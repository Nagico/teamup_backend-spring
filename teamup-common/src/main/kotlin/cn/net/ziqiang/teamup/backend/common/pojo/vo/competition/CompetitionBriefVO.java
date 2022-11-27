package cn.net.ziqiang.teamup.backend.common.pojo.vo.competition;

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Schema(description = "比赛简要信息")
public class CompetitionBriefVO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "名称")
    String name;

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

    public CompetitionBriefVO(Competition competition) {
        this.id = competition.getId();
        this.name = competition.getName();
        this.verified = competition.getVerified();
        this.logo = competition.getLogo();
        this.startTime = competition.getStartTime();
        this.endTime = competition.getEndTime();
    }
}
