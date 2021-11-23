import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.0"
  application
}

group = "me.reylibutan"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.slf4j:slf4j-simple:1.7.32")
  implementation("com.github.kittinunf.fuel:fuel:2.3.1")
  implementation("com.google.code.gson:gson:2.8.9")
  implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnit()
}

tasks.withType<KotlinCompile>() {
  kotlinOptions.jvmTarget = "1.8"
}

application {
  mainClass.set("MainKt")
}