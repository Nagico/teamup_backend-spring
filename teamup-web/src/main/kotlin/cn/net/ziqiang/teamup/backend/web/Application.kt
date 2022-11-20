package cn.net.ziqiang.teamup.backend.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["cn.net.ziqiang.teamup.backend"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
