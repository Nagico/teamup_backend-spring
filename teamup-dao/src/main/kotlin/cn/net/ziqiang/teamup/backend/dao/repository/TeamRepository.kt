package cn.net.ziqiang.teamup.backend.dao.repository;

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByNameContaining(String name);

    List<Team> findAllByCompetitionId(Long competitionId);

    List<Team> findAllByCompetitionNameContaining(String competitionName);

    List<Team> findAllByLeaderId(Long creatorId);
}
