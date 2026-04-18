plugins {
    alias(libs.plugins.custom.android.library.compose)
    alias(libs.plugins.custom.android.feature)
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.feature.savingGroups)
    implementation(projects.feature.debtGroups)
}