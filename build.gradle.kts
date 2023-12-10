plugins {
    kotlin("plugin.serialization") version Kotlin.version
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Build.androidBuildTools)
        classpath(Build.kotlinGradlePlugin)
        classpath(Build.hiltAndroidGradlePlugin)
//        classpath("com.google.gms:google-services:4.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}")

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}