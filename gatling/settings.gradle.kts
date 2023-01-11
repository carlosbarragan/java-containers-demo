pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {

        val kotlinVersion: String by settings
        val gatlingPluginVersion: String by settings

        kotlin("jvm") version kotlinVersion

        id("io.gatling.gradle") version gatlingPluginVersion
    }
}

val projectName: String by extra
rootProject.name=projectName
