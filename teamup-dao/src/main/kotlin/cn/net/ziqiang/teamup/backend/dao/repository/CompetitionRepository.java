package cn.net.ziqiang.teamup.backend.dao.repository;

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findAllByName(String name);
}
