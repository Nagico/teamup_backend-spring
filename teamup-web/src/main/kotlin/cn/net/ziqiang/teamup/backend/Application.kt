package cn.net.ziqiang.teamup.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["cn.net.ziqiang.teamup.backend"])
@ConfigurationPropertiesScan(basePackages = ["cn.net.ziqiang.teamup.backend"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
