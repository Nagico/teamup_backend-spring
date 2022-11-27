package cn.net.ziqiang.teamup.backend.common.pojo.vo.competition;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "比赛审核信息修改结果")
public class CompetitionVerificationVO {
    private boolean verified;

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public CompetitionVerificationVO(boolean verified) {
        this.verified = verified;
    }
}
