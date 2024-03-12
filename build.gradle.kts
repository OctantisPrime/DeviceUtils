plugins {
    id("com.android.application") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "7.4.2" apply false
}

buildscript {
    val agp_version by extra("7.4.2")
    repositories {
        google()
        mavenCentral()
        maven(
            uri("https://jitpack.io")
        )
        maven(
            uri("https://maven.aliyun.com/repository/public")
        )
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
    }
}


