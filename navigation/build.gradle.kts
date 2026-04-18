plugins {
    alias(libs.plugins.custom.android.library.compose)
    alias(libs.plugins.custom.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.custom.hilt)
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.shared)
    implementation(projects.feature.createGroup)
    implementation(projects.feature.qrScanner)
    implementation(projects.feature.savingGroups)
    implementation(projects.feature.contribute)
    implementation(projects.feature.transaction)


    implementation(projects.core)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    api(libs.hilt.navigation.compose)
}