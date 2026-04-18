import com.flexcil.flexc.convention.AppConfig
import com.flexcil.flexc.convention.configureBuildAppOutputs
import com.flexcil.flexc.convention.configureKotlinAndroid
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            configureBuildAppOutputs()

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = AppConfig.TARGET_SDK
                defaultConfig.versionCode = AppConfig.VERSION_CODE
                defaultConfig.versionName = AppConfig.VERSION_NAME
                defaultConfig.applicationId = AppConfig.APPLICATION_ID

                configureKotlinAndroid(this)

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        isShrinkResources = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
        }
    }
}