plugins {
    alias(libs.plugins.custom.android.application.compose)
    alias(libs.plugins.custom.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.custom.hilt)
}

dependencies {
    implementation(projects.core)
    implementation(projects.navigation)

    implementation(projects.feature.home)

    implementation(projects.domain)
    implementation(projects.data)

    implementation(libs.androidx.activity.compose)
}