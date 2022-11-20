dependencies {
    implementation(project(":teamup-common"))
    implementation(project(":teamup-dao"))

    //Spring doc
    implementation("org.springdoc:springdoc-openapi-ui")
    runtimeOnly("org.springdoc:springdoc-openapi-kotlin")

    // MyBatis
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Wechat
    implementation("com.github.binarywang:weixin-java-miniapp:4.4.0")
}