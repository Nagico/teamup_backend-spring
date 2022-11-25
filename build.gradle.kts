import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream

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
	mainClass.set("cn.net.ziqiang.teamup.backend.Application")
}

allprojects {
	repositories {
		maven {
			setUrl("https://mirrors.163.com/maven/repository/maven-public/")
			content {
				includeGroup("io.sentry")
			}
		}
		maven {
			setUrl("https://maven.aliyun.com/repository/public")
		}
		maven {
			setUrl("https://maven.aliyun.com/repository/spring")
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

			//MyBatis
			mavenBom("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.2")

			// MyBatis Plus
			mavenBom("com.baomidou:mybatis-plus-boot-starter:3.5.2")

			// MyBatis Plus Generator
			mavenBom("com.baomidou:mybatis-plus-generator:3.5.3")
			mavenBom("org.freemarker:freemarker:2.3.31")
		}
	}

	dependencies {
		// Springboot
		implementation("org.springframework.boot:spring-boot-starter-web")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
		implementation("org.springframework.boot:spring-boot-starter-validation")

		// Kotlin
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

		// JSON
		implementation("com.alibaba:fastjson")
		implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")

		//JPA
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")

		//hutool
		implementation("cn.hutool:hutool-all:5.8.7")

		// MYSQL
		runtimeOnly("com.mysql:mysql-connector-j")

		// Sentry
		implementation("io.sentry:sentry-spring-boot-starter:6.8.0")

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

	tasks.bootJar {
		enabled = false
	}

	tasks.jar {
		enabled = true
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

buildscript {
	dependencies {
		classpath("org.yaml:snakeyaml:1.17")
		classpath("com.fasterxml.jackson.core:jackson-core:2.13.4")
	}
}

val dockerComposeDevFilePath = "docker/dev/docker-compose-dev.yml"
val dockerComposeUnitTestFilePath = "docker/unit-test/docker-compose-unit-test.yml"

tasks.register("buildTestEnvironmentAndTest") {

}

tasks.register("startDevEnvironment") {
	group = "dev docker"
	description = "开启开发环境下的运行环境"
	doFirst {
		exec {
			commandLine("docker", "compose", "-f", dockerComposeDevFilePath, "up", "-d")
		}
	}
}

tasks.register("resetDevEnvironment") {
	group = "dev docker"
	description = "重置开发环境下的运行环境"
	doFirst {
		listOf(
			arrayOf("docker", "compose", "-f", dockerComposeDevFilePath, "stop"),
			arrayOf("docker", "compose", "-f", dockerComposeDevFilePath, "rm", "-f")
		).forEach {
			exec {
				commandLine(*it)
			}
		}
	}
}

tasks.register("stopDevEnvironment") {
	group = "dev docker"
	description = "停止开发环境下的运行环境"
	doFirst {
		exec {
			commandLine("docker", "compose", "-f", dockerComposeDevFilePath, "stop")
		}
	}
}

tasks.register("cleanAll") {
	group = "build"
	description = "清除所有build"

	dependsOn(
		*subprojects.map { it.tasks.named("clean") }
			.toMutableList()
			.apply {
				add(tasks.named("clean"))
			}
			.toTypedArray()
	)
}

//region DevelopmentBuild
tasks.register("buildDevJar") {
	group = "build dev"
	description = "构建开发版本的Jar包"



	val dependsList = mutableListOf<Any>()

	//前置添加docker测试环境
	dependsList.add(tasks.named("buildDevPreTask"))

	//clean
	dependsList.add(tasks.named("cleanAll"))

	//构建
	dependsList.addAll(subprojects.map { it.tasks.named("build") })
	dependsOn(*dependsList.toTypedArray())

	doFirst {
		//构建完成，移动构建jar包
		//删除之前的文件夹
		mkdir("target")
		delete("target/app.jar")
		delete("target/lib")

		copy {
			from("teamup-web/build/libs/teamup-web.jar")
			into("target")
			rename("teamup-web.jar", "app.jar")
		}

		copy {
			from("teamup-web/build/libs/lib")
			into("target/lib")
		}

		if (project.file("target/app.jar").exists()) {
			println("Create JAR file successfully in: ${System.getProperty("user.dir")}/target/app.jar")
		} else {
			error("Create JAR file failed")
		}

	}

	finalizedBy("buildDevPostTask")
}

tasks.register("buildDevPostTask") {
	group = "build dev"
	description = "构建开发版本Jar包后置步骤"

	doFirst {
	}
}

tasks.register("buildDevPreTask") {
	group = "build dev"
	description = "构建开发版本Jar包前置步骤"
	doFirst {
	}
}
//endregion

//region ReleaseBuild
tasks.register("buildReleaseJar") {
	group = "build release"
	description = "构建发布版本的Jar包"

	val dependsList = mutableListOf<Any>()

	//前置添加docker测试环境
	dependsList.add(tasks.named("buildReleasePreTask"))

	//clean
	dependsList.add(tasks.named("cleanAll"))

	//构建
	dependsList.addAll(subprojects.map { it.tasks.named("build") })
	dependsOn(*dependsList.toTypedArray())

	doFirst {
		//构建完成，移动构建jar包
		//删除之前的文件夹
		mkdir("target")
		delete("target/app.jar")
		delete("target/lib")

		copy {
			from("teamup-web/build/libs/teamup-web.jar")
			into("target")
			rename("teamup-web.jar", "app.jar")
		}

		copy {
			from("teamup-web/build/libs/lib")
			into("target/lib")
		}

		if (project.file("target/app.jar").exists()) {
			println("Create JAR file successfully in: ${System.getProperty("user.dir")}/target/app.jar")
		} else {
			error("Create JAR file failed")
		}

	}

	this.finalizedBy("buildReleasePostTask")
}

tasks.register("buildReleasePostTask") {
	group = "build release"
	description = "构建发布版本Jar包后置步骤"

	doFirst {
		stopUnitTestEnvironment()
	}
}

tasks.register("buildReleasePreTask") {
	group = "build release"
	description = "构建发布版本Jar包前置步骤"
	doFirst {

		//1. 测试是否开启生产模式
		val file =
			FileInputStream(File("${project.projectDir}/ev-web/src/main/resources/application.yml"))
		val yml = org.yaml.snakeyaml.Yaml()

		var prod = false
		for (data in yml.loadAll(file)) {
			val map = data as Map<*, *>
			with(com.fasterxml.jackson.databind.ObjectMapper()) {
				val jsonStr = writeValueAsString(map)
				val profile = readTree(jsonStr)["spring"]["profiles"]["active"]?.asText() ?: ""
				if (profile == "prod") {
					prod = true
				}
			}

			if (prod) {
				break
			}
		}

		if (!prod) {
			error("未开启生产模式")
		}


		//2. 开启测试环境
		startUnitTestEnvironment()
	}
}
//endregion

//region verification
tasks.register("startUnitTestEnvironment") {
	group = "verification"
	doFirst {
		stopUnitTestEnvironment()
		startUnitTestEnvironment()
	}
}

tasks.register("stopUnitTestEnvironment") {
	group = "verification"
	doFirst {
		stopUnitTestEnvironment()
	}
}

fun startUnitTestEnvironment() {
	println("Starting test env...")

	listOf(
		arrayOf("docker", "version"),
		arrayOf("docker", "compose", "version"),
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "stop"),
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "rm", "-f"),
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "up", "-d"),
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "ps")
	).forEach { commands ->
		exec {
			commandLine(*commands)
		}
	}
}

fun stopUnitTestEnvironment() {
	println("Stopping test env...")

	listOf(
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "ps"),
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "stop"),
		arrayOf("docker", "compose", "-f", dockerComposeUnitTestFilePath, "rm", "-f"),
	).forEach { commands ->
		exec {
			commandLine(*commands)
		}
	}
	println("Stop test env")
}
//endregion
