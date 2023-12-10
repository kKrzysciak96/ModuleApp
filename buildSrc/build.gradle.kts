import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    implementation("com.android.tools.build:gradle:8.1.1")
    implementation("com.squareup:javapoet:1.13.0")

}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "18"
}