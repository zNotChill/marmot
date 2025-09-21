pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "marmot"

include(":common")
include(":mod")
include(":servers:minestom")

project(":common").projectDir = file("common")
project(":mod").projectDir = file("mod")
project(":servers:minestom").projectDir = file("servers/minestom")