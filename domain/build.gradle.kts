plugins {
   alias(libs.plugins.custom.android.library)
   alias(libs.plugins.custom.hilt)
}

dependencies {
    implementation(projects.core)
}