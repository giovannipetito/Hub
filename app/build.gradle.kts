plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // alias(libs.plugins.kotlin.parcelize)
    id("kotlin-parcelize")
    // alias(libs.plugins.kotlin.kapt)
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.relay) // Figma
    // alias(libs.plugins.secrets.gradle.plugin)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services") // Google services Gradle plugin
}

android {
    namespace = "it.giovanni.hub"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.giovanni.hub"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            buildConfigField("String", "BASE_URL", "\"https://reqres.in\"")
            // buildConfigField("String", "OPEN_AI_API_KEY", "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")
            // buildConfigField("String", "GOOGLE_AI_API_KEY", "AIzaSyAQmdldTPa3oYe4xIjTC8IMWNiw9f96z2Y")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL", "\"https://reqres.in\"")
            // buildConfigField("String", "OPEN_AI_API_KEY", "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")
            // buildConfigField("String", "GOOGLE_AI_API_KEY", "AIzaSyAQmdldTPa3oYe4xIjTC8IMWNiw9f96z2Y")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        // Or shorter:
        // jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    // Figma assets directory.
    sourceSets {
        getByName("main") {
            assets {
                srcDir("src/main/ui-packages")
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.kotlin.bom))

    // Material
    implementation(libs.androidx.material.android)
    implementation(libs.androidx.material3.android)

    // The Accompanist library is deprecated.
    implementation(libs.accompanist.webview) // WebView
    implementation(libs.accompanist.permissions) // Permissions

    // Foundation - System UI Controller
    implementation(libs.androidx.foundation)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences.android)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // GSON
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.scalars)
    implementation(libs.retrofit2.rxjava2.adapter)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Reactive Extensions
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Coil
    implementation(libs.coil.compose)

    // Dependency Injection - Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.android.compiler)

    // Room Database
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava2)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Paging 3.0
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.rxjava3)
    implementation(libs.androidx.paging.runtime.ktx)

    // Splash API
    implementation(libs.androidx.core.splashscreen)

    // Lottie
    implementation(libs.lottie.compose)

    // Fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Dependency for the Google AI client SDK for Android
    implementation(libs.generativeai)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.testng)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}