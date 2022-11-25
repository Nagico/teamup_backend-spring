package cn.net.ziqiang.teamup.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["cn.net.ziqiang.teamup.backend"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
