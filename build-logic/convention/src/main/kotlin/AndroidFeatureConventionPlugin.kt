import com.flexcil.flexc.convention.implementation
import com.flexcil.flexc.convention.implementationLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("custom.android.library")
                apply("custom.hilt")
            }

            dependencies {
                implementation(project(":domain"))
                implementation(project(":core"))

                implementationLib("androidx.lifecycle.viewmodel.compose")
                implementationLib("androidx.lifecycle.runtime.compose")
                implementationLib("kotlin.serialization.json")
                implementationLib("hilt-navigation-compose")
            }
        }
    }
}