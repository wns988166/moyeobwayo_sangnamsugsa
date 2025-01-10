// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    val kotlinVersion = "1.8.10"
    val kotlinxSerializationVersion = "1.5.1"
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io")}
        }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion") // 추가한 부분
    }
}