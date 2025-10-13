plugins {
    `maven-publish`
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
}

val mavenGroup = property("maven_group") as String
group = mavenGroup
version = property("minestom_api_version") as String

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.znotchill.me/repository/maven-releases/") }
}

dependencies {
    implementation(project(":common"))
    testImplementation(kotlin("test"))
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
    implementation("me.znotchill:blossom:1.4.4")
    implementation("io.github.xn32:json5k:0.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
    implementation("net.kyori:adventure-api:4.24.0")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
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
            artifactId = "minestom-api"
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