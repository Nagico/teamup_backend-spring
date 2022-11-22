dependencies {
    implementation(project(":teamup-common"))
    implementation(project(":teamup-service"))

    //Springboot Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    //Spring doc
    implementation("org.springdoc:springdoc-openapi-ui")
    runtimeOnly("org.springdoc:springdoc-openapi-kotlin")
}

//支持启动类
tasks.bootJar {enabled = true}
