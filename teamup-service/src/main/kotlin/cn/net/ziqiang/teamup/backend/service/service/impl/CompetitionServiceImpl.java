package cn.net.ziqiang.teamup.backend.service.service.impl;

import cn.net.ziqiang.teamup.backend.common.constant.type.ResultType;
import cn.net.ziqiang.teamup.backend.common.exception.ApiException;
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition;
import cn.net.ziqiang.teamup.backend.common.pojo.vo.competition.CompetitionBriefVO;
import cn.net.ziqiang.teamup.backend.common.pojo.vo.competition.CompetitionVerificationVO;
import cn.net.ziqiang.teamup.backend.dao.repository.CompetitionRepository;
import cn.net.ziqiang.teamup.backend.service.service.CompetitionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    CompetitionRepository competitionRepository;

    @Override
    public boolean competitionExists(Long id) {
        return competitionRepository.existsById(id);
    }

    @Override
    @NotNull
    public List<CompetitionBriefVO> getCompetitionList() {
        return competitionRepository.findAll().stream().map(CompetitionBriefVO::new).toList();
    }

    @Override
    @NotNull
    public Competition getCompetitionById(Long id) throws ApiException {
        return competitionRepository.findById(id).orElseThrow(() -> new ApiException(ResultType.ResourceNotFound, "比赛不存在"));
    }

    @Override
    public List<Competition> getCompetitionByName(String name) {
        return competitionRepository.findAllByName(name);
    }

    @Override
    public Competition addCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }

    @Override
    @NotNull
    public Competition updateCompetition(Long id, Competition competition) throws ApiException {
        Competition newCompetition = competitionRepository.findById(id).orElse(null);
        if (newCompetition == null) {
            throw new ApiException(ResultType.ResourceNotFound, "比赛不存在");
        }
        if (competition.getName() != null) {
            newCompetition.setName(competition.getName());
        }
        if (competition.getDescription() != null) {
            newCompetition.setDescription(competition.getDescription());
        }
        if (competition.getLogo() != null) {
            newCompetition.setLogo(competition.getLogo());
        }
        if (competition.getStartTime() != null) {
            newCompetition.setStartTime(competition.getStartTime());
        }
        if (competition.getEndTime() != null) {
            newCompetition.setEndTime(competition.getEndTime());
        }
        if (competition.getVerified() != null) {
            newCompetition.setVerified(competition.getVerified());
        }
        if (competition.getStartTime() != null) {
            newCompetition.setStartTime(competition.getStartTime());
        }
        if (competition.getEndTime() != null) {
            newCompetition.setEndTime(competition.getEndTime());
        }
        if (competition.getFinish() != null) {
            newCompetition.setFinish(competition.getFinish());
        }
        if (competition.getScore() != null) {
            newCompetition.setScore(competition.getScore());
        }
        return competitionRepository.save(newCompetition);
    }

    @Override
    public void deleteCompetitionById(Long id) {
        competitionRepository.deleteById(id);
    }

    @Override
    public boolean setVerified(Long id, boolean verified) throws ApiException {
        Competition competition = competitionRepository.findById(id).orElse(null);
        if (competition == null) {
            throw new ApiException(ResultType.ResourceNotFound, "比赛不存在");
        }
        competition.setVerified(verified);
        competitionRepository.save(competition);
        return verified;
    }
}
