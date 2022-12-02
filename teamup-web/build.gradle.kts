dependencies {
    //Spring doc
    implementation("org.springdoc:springdoc-openapi-ui")
    runtimeOnly("org.springdoc:springdoc-openapi-kotlin")

    //JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // 阿里云OSS
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.15.2")

    // 阿里云SMS
    implementation("com.aliyun:alibabacloud-dysmsapi20170525:2.0.22")

    // MyBatis
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // rabbitmq
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // stomp
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")

    // Wechat
    implementation("com.github.binarywang:weixin-java-miniapp:4.4.0")

    //Springboot Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // rabbitmq
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // stomp
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
}

tasks.register("copyLib") {
    doLast {
        println(configurations.runtimeClasspath)

        delete("$buildDir/libs/lib")

        copy {
            from(configurations.runtimeClasspath)
            into("$buildDir/libs/lib")
        }
    }
}

//支持启动类
tasks.bootJar {
    enabled = true
    exclude("*.jar")

    // 指定依赖包的路径
    manifest {
        attributes(
            "Manifest-Version" to 1.0,
            "Class-Path" to configurations.runtimeClasspath.get().files.joinToString(" ") { "lib/${it.name}" }
        )
    }

    finalizedBy("copyLib")
}
