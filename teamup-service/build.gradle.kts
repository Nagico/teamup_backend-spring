dependencies {
    implementation(project(":teamup-common"))
    implementation(project(":teamup-dao"))

    // MyBatis
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Wechat
    implementation("com.github.binarywang:weixin-java-miniapp:4.4.0")
}