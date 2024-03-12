plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.2.1" apply false
}

buildscript {
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
        classpath("com.android.tools.build:gradle:8.2.2")
    }
}


