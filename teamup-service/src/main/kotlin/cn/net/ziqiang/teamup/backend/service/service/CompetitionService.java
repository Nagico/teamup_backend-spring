package cn.net.ziqiang.teamup.backend.service.service;

import cn.net.ziqiang.teamup.backend.common.exception.ApiException;
import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition;
import cn.net.ziqiang.teamup.backend.common.pojo.vo.competition.CompetitionBriefVO;

import java.util.List;

public interface CompetitionService {

    boolean competitionExists(Long id);

    /**
     * 获取比赛列表
     * @return
     */
    List<CompetitionBriefVO> getCompetitionList();

    /**
     * 根据id获取比赛
     * @param id 比赛id
     * @return 比赛
     */
    Competition getCompetitionById(Long id) throws ApiException;

    /**
     * 根据名称获取比赛列表
     * @return 比赛列表
     */
    List<Competition> getCompetitionByName(String name);

    /**
     * 添加比赛
     * @return 新的比赛
     */
    Competition addCompetition(Competition competition);

    /**
     * 更新比赛
     * @param id 比赛id
     * @param competition 比赛
     * @return 更新后的比赛
     */
    Competition updateCompetition(Long id, Competition competition) throws ApiException;

    /**
     * 删除比赛
     * @param id 比赛id
     */
    void deleteCompetitionById(Long id);
}
