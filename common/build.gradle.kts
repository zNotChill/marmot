plugins {
    `maven-publish`
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    `java-library`
}

val mavenGroup = property("maven_group") as String
group = mavenGroup
version = property("common_version") as String

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("net.kyori:adventure-api:4.24.0")
    implementation("org.joml:joml:1.10.8")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = mavenGroup
            artifactId = "common"
            version
        }
    }

    repositories {
        maven {
            name = "znotchill"
            url = uri("https://repo.znotchill.me/repository/maven-releases/")
            credentials {
                username = findProperty("zRepoUsername") as String? ?: System.getenv("MAVEN_USER")
                password = findProperty("zRepoPassword") as String? ?: System.getenv("MAVEN_PASS")
            }
        }
        mavenLocal()
    }
}