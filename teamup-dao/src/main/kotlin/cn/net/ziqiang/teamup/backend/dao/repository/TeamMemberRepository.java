package cn.net.ziqiang.teamup.backend.dao.repository;

import cn.net.ziqiang.teamup.backend.common.pojo.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

}
