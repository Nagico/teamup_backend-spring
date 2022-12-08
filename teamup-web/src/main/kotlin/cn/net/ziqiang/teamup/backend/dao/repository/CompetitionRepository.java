package cn.net.ziqiang.teamup.backend.dao.repository;

import cn.net.ziqiang.teamup.backend.pojo.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findAllByName(String name);

    List<Competition> findAllByIdIn(Set<Long> ids);
}
