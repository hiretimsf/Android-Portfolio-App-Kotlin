plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.navigation.safeargs)
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
    namespace = libs.versions.appId.get()
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

            resValue("string", "google_maps_key", (project.findProperty("GOOGLE_MAPS_API_KEY") ?: "").toString())
            versionNameSuffix = "-debug"
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
            resValue("string", "google_maps_key", (project.findProperty("GOOGLE_MAPS_API_KEY") ?: "").toString())
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
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
    implementation(libs.google.map)
    implementation(libs.google.guava)

    // AndroidX Foundation
    implementation(libs.androidx.core)
    implementation(libs.androidx.legacy)
    implementation(libs.androidx.futures)
    implementation(libs.androidx.vector)
    implementation(libs.androidx.constraint)
    implementation(libs.androidx.viewpager)
    implementation(libs.androidx.emoji)
    implementation(libs.androidx.preferences)
    implementation(libs.androidx.recyclerview)

    // Activity
    implementation(libs.activity.appcompat)

    // Fragment
    implementation(libs.fragment.ktx)

    // Android Architecture components
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.saved.state)
    implementation(libs.lifecycle.runtime)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    kapt(libs.room.compiler)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.paging.runtime)
    implementation(libs.work.runtime)
    implementation(libs.hilt.androidx.work)

    // Firebase components
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.core)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.appindex)
    implementation(libs.firebase.monitoring)

    // Dependency injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.fragment)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.androidx.compiler)
    implementation(libs.splash.screen)

    // Network components
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.coroutines)
    implementation(libs.gson.core)
    implementation(libs.gson.converter)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp.okio)
    implementation(libs.coil.core)
    implementation(libs.stetho.core)
    implementation(libs.stetho.okhttp)

    // Utilities
    implementation(libs.sdp)
    implementation(libs.ink.page.indicator)
    implementation(libs.androidx.browser)
    implementation(libs.toasty)
    implementation(libs.shape.view)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test)
    androidTestImplementation(libs.androidx.espresso)
    testImplementation(libs.fragment.test)
    testImplementation(libs.room.test)
    testImplementation(libs.androidx.arch.test)
    testImplementation(libs.paging.test)
    androidTestImplementation(libs.work.test)
}
