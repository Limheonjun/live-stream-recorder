import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("com.google.cloud.tools.jib") version "3.4.0"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.emgc"
version = "snapeshot-${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")

    // Youtube
    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")

    // Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
    archiveFileName.set("app.jar")
}

tasks.named("build") {
    dependsOn("clean")
    mustRunAfter("clean")
}

tasks.named("jib") {
    dependsOn("build")
}

jib {
    from {
        image = "${System.getProperty("registry.url")}/live-stream-recorder-base-image"
    }
    to {
        image = "${System.getProperty("registry.url")}/live-stream-recorder"
        tags = setOf("${project.version}", "latest")
        setAllowInsecureRegistries(true)
    }
    container {
        jvmFlags = listOf(
            "-Dfile.encoding=UTF-8",
            "-Dspring.profiles.active=prod",
            "-XX:InitialRAMPercentage=50.0",
            "-XX:MaxRAMPercentage=80.0",
            "-XX:+UseG1GC",
            "-XX:+HeapDumpOnOutOfMemoryError",
            "-XX:HeapDumpPath=/tmp",
            "-XX:+ExitOnOutOfMemoryError",
            "-Xlog:gc*:file=/tmp/gc.log:time,uptime,level,tags"
        )
        ports = listOf("8080")
    }
}