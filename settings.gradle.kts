pluginManagement {
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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(
            uri("https://jitpack.io")
        )
        maven(
            uri("https://maven.aliyun.com/repository/public")
        )

        mavenCentral()
    }
}

rootProject.name = "DeviceUtils"
include(":app")
include(":devicelib")
