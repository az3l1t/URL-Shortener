plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "net.azelit"
version = "0.0.1-SNAPSHOT"

jacoco {
	toolVersion = "0.8.7"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging.showStandardStreams = true
}

val test by tasks.getting(Test::class) { }

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.test)
	violationRules {
		rule {
			element = "CLASS"
			includes = listOf("net.azelit.url.service.*")
			excludes = listOf("net.azelit.url.client.*",
				"net.azelit.url.controller.*",
				"net.azelit.url.repository.*",
				"net.azelit.url.mapper.*")
			limit {
				minimum = 0.2.toBigDecimal()
			}
		}
	}

	filter {
		exclude("net/azelit/url/**")
	}
}

tasks.withType<JacocoReport> {
	val filteredClassDirectories = classDirectories.files.map { dir ->
		project.fileTree(dir) {
			exclude(
				"net/azelit/user_service/client",
				"net/azelit/user_service/config",
				"net/azelit/user_service/controller",
				"net/azelit/user_service/dto",
				"net/azelit/user_service/mapper"
			)
		}
	}
	classDirectories.setFrom(filteredClassDirectories)
}

tasks {
	val jacocoCustomTestReport by creating(JacocoReport::class) {
		reports {
			xml.required.set(false)
			csv.required.set(false)
			html.required.set(true)
		}
	}

	withType<Test> {
		finalizedBy(jacocoCustomTestReport)
	}
}