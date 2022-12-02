package cn.net.ziqiang.teamup.backend.util.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["cn.net.ziqiang.teamup.backend.pojo.entity"])
@EnableJpaRepositories("cn.net.ziqiang.teamup.backend.dao.repository")
class DaoConfig
