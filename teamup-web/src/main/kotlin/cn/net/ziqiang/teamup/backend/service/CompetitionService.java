package cn.net.ziqiang.teamup.backend.service;

import cn.net.ziqiang.teamup.backend.pojo.exception.ApiException;
import cn.net.ziqiang.teamup.backend.pojo.entity.Competition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CompetitionService {

    /**
     * 判断比赛是否存在
     * @param id 比赛id
     * @return 是否存在
     */
    boolean competitionExists(Long id);

    /**
     * 获取比赛列表
     * @return
     */
    @NotNull
    List<Competition> getCompetitionList();

    /**
     * 根据id获取比赛
     * @param id 比赛id
     * @return 比赛
     */
    @NotNull
    Competition getCompetitionById(Long id) throws ApiException;

    /**
     * 根据名称获取比赛列表
     * @return 比赛列表
     */
    @NotNull
    List<Competition> getCompetitionByName(String name);

    /**
     * 添加比赛
     * @return 新的比赛
     */
    @NotNull
    Competition addCompetition(Competition competition);

    /**
     * 更新比赛
     * @param id 比赛id
     * @param competition 比赛
     * @return 更新后的比赛
     */
    @NotNull
    Competition updateCompetition(Long id, Competition competition) throws ApiException;

    /**
     * 删除比赛
     * @param id 比赛id
     */
    void deleteCompetitionById(Long id);

    /**
     *
     * @param id
     * @param verified
     * @return
     * @throws ApiException
     */
    Competition setVerified(Long id, boolean verified) throws ApiException;
}
