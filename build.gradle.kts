import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "cn.net.ziqiang"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

springBoot {
	mainClass.value("cn.net.ziqiang.teamup.backend.Application")
}

allprojects {
	repositories {
		maven {
			setUrl("https://maven.aliyun.com/repository/public/")
		}
		maven {
			setUrl("https://maven.aliyun.com/repository/spring/")
		}
		maven {
			setUrl("https://maven.aliyun.com/repository/spring-plugin")
		}
		maven {
			setUrl("https://maven.aliyun.com/repository/gradle-plugin")
		}
		mavenCentral()
	}
}

subprojects {
	apply {
		plugin("org.springframework.boot")
		plugin("io.spring.dependency-management")
		plugin("org.jetbrains.kotlin.jvm")
		plugin("org.jetbrains.kotlin.plugin.spring")
		plugin("org.jetbrains.kotlin.plugin.allopen")
	}

	allOpen {
		annotation("javax.persistence.Entity")
		annotation("javax.persistence.MappedSuperclass")
		annotation("javax.persistence.Embeddable")
	}

	dependencyManagement {
		imports {
			//Spring Doc
			mavenBom("org.springdoc:springdoc-openapi-ui:1.6.12")
			mavenBom("org.springdoc:springdoc-openapi-kotlin:1.6.12")

			//OkHttp
			mavenBom("com.squareup.okhttp3:okhttp-bom:4.10.0")

			// JSON
			mavenBom("com.alibaba:fastjson:2.0.15")

			// JWT
			mavenBom("io.jsonwebtoken:jjwt:0.9.1")
		}
	}

	dependencies {
		// Springboot
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-security")
		implementation("org.springframework.boot:spring-boot-starter-cache")
		implementation("org.springframework.boot:spring-boot-starter-data-redis")
		implementation("org.springframework.boot:spring-boot-configuration-processor")

		// Kotlin
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		// Spring Doc
		implementation("org.springdoc:springdoc-openapi-ui")
		runtimeOnly("org.springdoc:springdoc-openapi-kotlin")

		// JSON
		implementation("com.alibaba:fastjson")

		// JWT
		implementation("io.jsonwebtoken:jjwt")

		// MYSQL
		runtimeOnly("com.mysql:mysql-connector-j")

		// 测试
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.springframework.security:spring-security-test")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
			java
		}
		inputs.files(tasks.named("processResources"))
	}


	tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
		enabled = false

		doLast {

		}
	}

	tasks.getByName<Jar>("jar") {
		enabled = true
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
