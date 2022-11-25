dependencies {
    implementation(project(":teamup-common"))
    implementation(project(":teamup-dao"))

    //Spring doc
    implementation("org.springdoc:springdoc-openapi-ui")
    runtimeOnly("org.springdoc:springdoc-openapi-kotlin")

    // 阿里云OSS
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.15.2")

    // 阿里云SMS
    implementation("com.aliyun:alibabacloud-dysmsapi20170525:2.0.22")

    // MyBatis
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Wechat
    implementation("com.github.binarywang:weixin-java-miniapp:4.4.0")
}