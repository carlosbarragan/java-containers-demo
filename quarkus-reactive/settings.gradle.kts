pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {

        val kotlinVersion: String by settings
        val quarkusPluginVersion: String by settings
        val quarkusPluginId: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
        kotlin("plugin.noarg") version kotlinVersion

        id(quarkusPluginId) version quarkusPluginVersion
    }
}
val projectName: String by extra
rootProject.name=projectName
