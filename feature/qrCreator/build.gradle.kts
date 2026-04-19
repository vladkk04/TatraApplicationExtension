plugins {
    alias(libs.plugins.custom.android.library.compose)
    alias(libs.plugins.custom.android.feature)
}

dependencies {
    implementation(projects.core)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
}
