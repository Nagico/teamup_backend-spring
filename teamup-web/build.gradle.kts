dependencies {
    implementation(project(":teamup-common"))
    implementation(project(":teamup-service"))

    //Springboot Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // rabbitmq
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // stomp
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")

    //Spring doc
    implementation("org.springdoc:springdoc-openapi-ui")
    runtimeOnly("org.springdoc:springdoc-openapi-kotlin")
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
