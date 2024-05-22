// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.relay.gradle.plugin)
        classpath(libs.secrets.gradle.plugin)
    }
}

plugins {
    id("com.android.application") version "8.4.1" apply false
    id("com.android.library") version "8.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false
    id("com.google.relay") version "0.3.10" apply false
}