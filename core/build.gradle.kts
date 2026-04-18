plugins {
    alias(libs.plugins.custom.android.library.compose)
    alias(libs.plugins.custom.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.custom.hilt)
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.material3.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.google.mlkit.barcode)
    implementation(libs.androidx.camera.core)

    api(libs.datastore.preferences)
}