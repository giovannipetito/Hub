import java.util.Properties

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.com.google.gms.google.services)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.com.google.relay) // Figma
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "it.giovanni.hub"
    compileSdk = 35

    defaultConfig {
        applicationId = "it.giovanni.hub"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val apiKey = getLocalProperty("GEMINI_API_KEY", project)
        if (apiKey != null) {
            buildConfigField("String", "GEMINI_API_KEY", "\"$apiKey\"")
        } else {
            throw GradleException("GEMINI_API_KEY is not defined in local.properties")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            buildConfigField("String", "BASE_URL", "\"https://reqres.in\"")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL", "\"https://reqres.in\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    /*
    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
    */

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("META-INF/{AL2.0,LGPL2.1}")
            // excludes.add("META-INF/DEPENDENCIES")
            // excludes.add("META-INF/INDEX.LIST")
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

    // Needed for exportSchema = true in Room database.
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

/*
composeCompiler {
    enableStrongSkippingMode = true
}
*/

secrets {
    defaultPropertiesFileName = "local.properties"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.org.jetbrains.kotlin.bom))
    // implementation(libs.kotlin.stdlib)

    // UI
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ksp
    implementation(libs.com.google.devtools.ksp.symbol.processing.api)

    // Material
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)

    // Accompanist (deprecated)
    implementation(libs.com.google.accompanist.permissions)

    // Foundation - System UI Controller
    implementation(libs.androidx.compose.foundation)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences.android)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Serialization: Json
    implementation(libs.org.jetbrains.kotlinx.serialization.json)

    // Serialization: Gson
    implementation(libs.com.google.code.gson)

    // Retrofit2
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson)
    implementation(libs.com.squareup.retrofit2.adapter.rxjava3)
    implementation(libs.com.squareup.retrofit2.converter.scalars)

    // Okhttp3
    implementation(libs.com.squareup.okhttp3.okhttp)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)

    // RxJava3
    implementation(libs.io.reactivex.rxjava3.rxjava)
    implementation(libs.io.reactivex.rxjava3.rxkotlin)
    implementation(libs.io.reactivex.rxjava3.rxandroid)

    // Coroutines
    implementation(libs.org.jetbrains.kotlinx.coroutines.core)
    implementation(libs.org.jetbrains.kotlinx.coroutines.android)

    // Coil
    implementation(libs.io.coilkt.coil.compose)

    // Dependency Injection - Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.com.google.dagger.hilt.android.compiler)

    // Room Database
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava3)

    // Paging 3.0
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.rxjava3)
    implementation(libs.androidx.paging.runtime.ktx)

    // Splash API
    implementation(libs.androidx.core.splashscreen)

    // Lottie
    implementation(libs.com.airbnb.android.lottie.compose)

    // Google AI client SDK
    implementation(libs.com.google.ai.client.generativeai)

    // Google Play services
    implementation(libs.com.google.android.gms.play.services.auth)

    // Firebase
    implementation(platform(libs.com.google.firebase.bom))
    implementation(libs.com.google.firebase.analytics)
    implementation(libs.com.google.firebase.auth)
    implementation(libs.com.google.firebase.crashlytics)
    implementation(libs.com.google.firebase.database)

    // Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.com.google.android.libraries.identity.googleid)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // DateTime
    implementation(libs.org.jetbrains.kotlinx.datetime)

    // Adaptive Navigation
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)

    // Glance
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)

    testImplementation(libs.junit)
    testImplementation(libs.org.testng)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}

// Function to read properties from the local.properties file
fun getLocalProperty(propertyName: String, project: Project): String? {
    val localProperties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    return localProperties.getProperty(propertyName)
}