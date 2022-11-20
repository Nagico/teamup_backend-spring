dependencies {
    implementation(project(":teamup-common"))
    implementation(project(":teamup-pojo"))

    //okhttp
    implementation("com.squareup.okhttp3:okhttp")

    //redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}