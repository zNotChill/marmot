plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    `java-library`
}

group = "me.znotchill.marmot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("io.netty:netty-buffer:4.1.111.Final")
    implementation("io.netty:netty-common:4.1.111.Final")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}