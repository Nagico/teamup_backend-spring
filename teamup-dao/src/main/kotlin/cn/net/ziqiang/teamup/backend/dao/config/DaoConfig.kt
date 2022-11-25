package cn.net.ziqiang.teamup.backend.dao.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["cn.net.ziqiang.teamup.backend.common.pojo.entity"])
@EnableJpaRepositories("cn.net.ziqiang.teamup.backend.dao.repository")
@MapperScan(basePackages = ["cn.net.ziqiang.teamup.backend.dao.mapper"])
class DaoConfig
