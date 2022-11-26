package cn.net.ziqiang.teamup.backend.dao.repository;

import cn.net.ziqiang.teamup.backend.common.pojo.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
}
