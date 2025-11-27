// File: CarpoolingApp/build.gradle.kts (Project level)

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("com.google.gms:google-services:4.4.0")
    }
}

plugins {
    id("com.android.application") version "8.1.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}