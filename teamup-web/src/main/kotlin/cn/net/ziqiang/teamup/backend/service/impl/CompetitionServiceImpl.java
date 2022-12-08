package cn.net.ziqiang.teamup.backend.service.impl;

import cn.net.ziqiang.teamup.backend.constant.type.ResultType;
import cn.net.ziqiang.teamup.backend.dao.repository.TeamRepository;
import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException;
import cn.net.ziqiang.teamup.backend.pojo.entity.Competition;
import cn.net.ziqiang.teamup.backend.dao.repository.CompetitionRepository;
import cn.net.ziqiang.teamup.backend.cache.CompetitionCacheManager;
import cn.net.ziqiang.teamup.backend.pojo.vo.DateCountVO;
import cn.net.ziqiang.teamup.backend.service.CompetitionService;
import cn.net.ziqiang.teamup.backend.service.RecommendService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    final CompetitionRepository competitionRepository;

    final CompetitionCacheManager competitionCacheManager;

    final TeamRepository teamRepository;

    final RecommendService recommendService;

    public CompetitionServiceImpl(
            CompetitionRepository competitionRepository,
            CompetitionCacheManager competitionCacheManager,
            TeamRepository teamRepository,
            RecommendService recommendService) {
        this.competitionRepository = competitionRepository;
        this.competitionCacheManager = competitionCacheManager;
        this.teamRepository = teamRepository;
        this.recommendService = recommendService;
    }

    @Override
    public boolean competitionExists(Long id) {
        Competition cached = competitionCacheManager.getCompetitionCache(id);

        if (cached == null) {
            return competitionRepository.existsById(id);
        }
        return true;
    }

    @Override
    @NotNull
    public List<Competition> getCompetitionList() {
        List<Competition> cached = competitionCacheManager.getCompetitionListCache();

        if (cached == null) {
            cached = competitionRepository.findAll();
            competitionCacheManager.setCompetitionListCache(cached);
        }
        return cached.stream().toList();
    }

    @Override
    @NotNull
    public Competition getCompetitionById(@NotNull Long id, @Nullable Long userId) throws ApiException {
        Competition cached = competitionCacheManager.getCompetitionCache(id);

        if (cached == null) {
            cached = competitionRepository.findById(id).orElseThrow(() -> new ApiException(ResultType.ResourceNotFound, "比赛不存在"));
            competitionCacheManager.setCompetitionCache(cached);
        }

        if (userId != null) {
            cached.setSubscribed(recommendService.checkUserSubscribeCompetition(userId, id));
        }

        return cached;
    }

    @Override
    public List<DateCountVO> getTeamCountByCompetitionId(Long id) {
        return teamRepository.countGroupByCreateDateById(id);
    }

    @Override
    @NotNull
    public List<Competition> getCompetitionByName(String name) {
        return competitionRepository.findAllByName(name);
    }

    @Override
    @NotNull
    public Competition addCompetition(Competition competition) {
        competition.setVerified(false);
        Competition res = competitionRepository.save(competition);
        competitionCacheManager.setCompetitionCache(res);
        competitionCacheManager.deleteCompetitionListCache();
        return res;
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
        if (competition.getAbbreviation() != null) {
            newCompetition.setAbbreviation(competition.getAbbreviation());
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
        Competition res = competitionRepository.save(newCompetition);
        competitionCacheManager.setCompetitionCache(res);
        competitionCacheManager.deleteCompetitionListCache();
        return res;
    }

    @Override
    public void deleteCompetitionById(Long id) {
        competitionRepository.deleteById(id);
        competitionCacheManager.deleteCompetitionCache(id);
        competitionCacheManager.deleteCompetitionListCache();
    }

    @Override
    public Competition setVerified(Long id, boolean verified) throws ApiException {
        Competition competition = competitionRepository.findById(id).orElse(null);
        if (competition == null) {
            throw new ApiException(ResultType.ResourceNotFound, "比赛不存在");
        }
        competition.setVerified(verified);
        competitionRepository.save(competition);
        competitionCacheManager.setCompetitionCache(competition);
        competitionCacheManager.deleteCompetitionListCache();
        return competition;
    }
}
