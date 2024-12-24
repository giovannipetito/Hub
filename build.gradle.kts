// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.relay.gradle.plugin)
        classpath(libs.secrets.gradle.plugin)
        classpath(libs.com.google.devtools.ksp.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.jetbrains.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.relay) apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0" apply false
}