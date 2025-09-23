plugins {
    `maven-publish`
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
}

group = "me.znotchill.marmot"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(project(":common"))
    testImplementation(kotlin("test"))
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
    implementation("net.minestom:minestom:2025.09.13-1.21.8")
    implementation("me.znotchill:blossom:1.4.1")
    implementation("io.github.xn32:json5k:0.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
    implementation("net.kyori:adventure-api:4.24.0")
    implementation("org.slf4j:slf4j-simple:2.0.13")
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

            groupId = "me.znotchill.marmot"
            artifactId = "marmot-api-minestom"
            version
        }
    }

    repositories {
        mavenLocal()
    }
}