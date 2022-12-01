package cn.net.ziqiang.teamup.backend.web.controller;

import cn.net.ziqiang.teamup.backend.common.constant.UserRole;
import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType;
import cn.net.ziqiang.teamup.backend.common.exception.ApiException;
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition;
import cn.net.ziqiang.teamup.backend.common.pojo.vo.competition.CompetitionBriefVO;
import cn.net.ziqiang.teamup.backend.common.pojo.vo.competition.CompetitionVerificationVO;
import cn.net.ziqiang.teamup.backend.service.service.CompetitionService;
import cn.net.ziqiang.teamup.backend.web.annotation.role.AllowRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "比赛")
@RestController
@RequestMapping("/competitions")
public class CompetitionController {
    @Autowired
    CompetitionService competitionService;

    @PermitAll
    @Operation(summary = "获取比赛列表")
    @GetMapping("")
    public List<CompetitionBriefVO> getCompetitionList() {
        return competitionService.getCompetitionList();
    }

    @PermitAll
    @Operation(summary = "根据id获取比赛")
    @GetMapping("/{id}")
    public Competition getCompetitionById(@PathVariable Long id) throws ApiException {
        return competitionService.getCompetitionById(id);
    }

    @AllowRole(role = UserRole.Manager)
    @Operation(summary = "创建比赛")
    @PostMapping
    public Competition addCompetition(@Valid @RequestBody Competition competition) {
        return competitionService.addCompetition(competition);
    }

    @AllowRole(role = UserRole.Manager)
    @Operation(summary = "更新比赛")
    @PutMapping("/{id}")
    public Competition updateCompetition(@PathVariable Long id, @RequestBody Competition competition) throws ApiException {
        return competitionService.updateCompetition(id, competition);
    }

    @AllowRole(role = UserRole.Manager)
    @Operation(summary = "删除比赛")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompetition(@PathVariable Long id) throws ApiException {
        if (!competitionService.competitionExists(id)) {
            throw new ApiException(ResultType.ResourceNotFound, "比赛不存在");
        }
        competitionService.deleteCompetitionById(id);
    }

    @AllowRole(role = UserRole.Manager)
    @Operation(summary = "比赛审核通过")
    @PostMapping("/{id}/verify")
    public CompetitionVerificationVO verifyCompetition(@PathVariable Long id) throws ApiException {
        return new CompetitionVerificationVO(competitionService.setVerified(id, true));
    }

    @AllowRole(role = UserRole.Manager)
    @Operation(summary = "比赛审核不通过")
    @DeleteMapping("/{id}/verify")
    public CompetitionVerificationVO unVerifyCompetition(@PathVariable Long id) throws ApiException {
        return new CompetitionVerificationVO(competitionService.setVerified(id, false));
    }
}