plugins {
    alias(libs.plugins.versions)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.legacy.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.navigation.safeargs) apply false
    alias(libs.plugins.firebase.perf) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
