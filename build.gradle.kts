plugins {
    id("com.android.application") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "7.4.2" apply false
}

tasks{
    delete(rootProject.buildDir)
}



