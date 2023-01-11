plugins {
    kotlin("jvm")
    id("io.gatling.gradle")
}

gatling {
  // WARNING: options below only work when logback config file isn't provided
  logLevel = "WARN" // logback root level
  logHttp = io.gatling.gradle.LogHttp.NONE // set to 'ALL' for all HTTP traffic in TRACE, 'FAILURES' for failed HTTP traffic in DEBUG
}

repositories {
  mavenCentral()
}
