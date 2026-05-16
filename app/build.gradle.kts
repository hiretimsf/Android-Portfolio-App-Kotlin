plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
}

fun getVersionCode(
    majorVersion: Int,
    minorVersion: Int,
    patchVersion: Int,
    buildVersion: Int,
): Int = majorVersion * 10_000_000 + minorVersion * 100_000 + patchVersion * 1_000 + buildVersion

fun getVersionName(
    majorVersion: Int,
    minorVersion: Int,
    patchVersion: Int,
): String = "$majorVersion.$minorVersion.$patchVersion"

val appVersionMajor = libs.versions.versionMajor.get().toInt()
val appVersionMinor = libs.versions.versionMinor.get().toInt()
val appVersionPatch = libs.versions.versionPatch.get().toInt()
val appVersionBuild = libs.versions.versionBuild.get().toInt()

android {
    namespace = libs.versions.appNamespace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        versionCode = getVersionCode(appVersionMajor, appVersionMinor, appVersionPatch, appVersionBuild)
        versionName = getVersionName(appVersionMajor, appVersionMinor, appVersionPatch)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "LOGS", "true")
            buildConfigField("boolean", "DEV_ENVIRONMENT", "true")

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        release {
            buildConfigField("boolean", "LOGS", "false")
            buildConfigField("boolean", "DEV_ENVIRONMENT", "false")

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        compose = true
        resValues = true
    }

    packaging {
        resources {
            excludes += "META-INF/atomicfu.kotlin_module"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    // Kotlin
    implementation(libs.kotlin.stdlib)

    // Google
    implementation(libs.google.material)

    // AndroidX Foundation
    implementation(libs.androidx.core)
    implementation(libs.androidx.vector)

    // Activity
    implementation(libs.activity.appcompat)
    implementation(libs.activity.compose)

    // Android Architecture components
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.saved.state)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.runtime.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.navigation.compose)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Firebase components
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.core)
    implementation(libs.firebase.monitoring)
    implementation(libs.firebase.crashlytics)

    // Dependency injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    implementation(libs.splash.screen)

    // Network components
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.custom.activity.on.crash)

    // Utilities
    implementation(libs.sdp)
    implementation(libs.androidx.browser)
    implementation(libs.toasty)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso)
    testImplementation(libs.androidx.arch.test)
    testImplementation(libs.paging.test)
    debugImplementation(libs.compose.ui.tooling)
}
