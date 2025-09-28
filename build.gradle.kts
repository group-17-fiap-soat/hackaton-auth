plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("jacoco")
    kotlin("plugin.jpa") version "1.9.25"
    id("org.sonarqube") version "6.2.0.5505"
}

group = "hackaton"
version = "0.0.1-SNAPSHOT"
description = "Sistema de processamento de vídeos desenvolvido em Kotlin"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
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
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("com.nimbusds:nimbus-jose-jwt:9.37")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.kafka:kafka-clients")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}


tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
        html.required = true
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                // Include only usecases
                include("**/usecases/**")
                // Exclude ProcessVideoUseCase specifically
                exclude("**/usecases/process/ProcessVideoUseCase*")
            }
        })
    )
}


sonar {
    properties {
        property("sonar.projectKey", "hackaton-auth")
        property("sonar.projectName", "hackaton-auth")
        // Include only usecases in coverage
        property("sonar.coverage.inclusions", "**/usecases/**/*.kt")
        property("sonar.tests", "src/test/kotlin")
        property("sonar.kotlin.coverage.reportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.scanner.metadataFilePath", file("${layout.buildDirectory}/sonar/report-task.txt").absolutePath)

        // Exclude everything except usecases
        property("sonar.coverage.exclusions",
            "**/adapters/**/*.kt," +
                    "**/commons/**/*.kt," +
                    "**/entities/**/*.kt," +
                    "**/config/**/*.kt," +
                    "**/Application.kt," +
                    "**/usecases/process/ProcessVideoUseCase.kt," +
                    "**/*Test*.kt," +
                    "**/test/**"
        )
        property("sonar.exclusions", "**/*Test*.kt,**/test/**")
    }
}