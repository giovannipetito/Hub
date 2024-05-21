plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")
    id("com.google.relay") // Figma
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
        kotlinCompilerExtensionVersion = "1.5.5"
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
    implementation("androidx.compose.ui:ui:1.6.7")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.ui:ui-graphics:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.0"))
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))

    // Material
    implementation("androidx.compose.material:material-android:1.6.7")
    implementation("androidx.compose.material3:material3-android:1.2.1")

    // The Accompanist library is deprecated.
    implementation("com.google.accompanist:accompanist-permissions:0.34.0") // Permissions
    implementation("com.google.accompanist:accompanist-webview:0.34.0") // WebView

    // Foundation - System UI Controller
    implementation("androidx.compose.foundation:foundation:1.6.7")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences-android:1.1.1")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // GSON
    implementation("com.google.code.gson:gson:2.11.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

    // Reactive Extensions
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Dependency Injection - Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    // Room Database
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-rxjava2:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // KotlinX Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Paging 3.0
    implementation("androidx.paging:paging-compose:3.3.0")
    implementation("androidx.paging:paging-rxjava3:3.3.0")
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.7")

    // Dependency for the Google AI client SDK for Android
    implementation("com.google.ai.client.generativeai:generativeai:0.6.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.7")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.testng:testng:7.10.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
}