import com.flexcil.flexc.convention.implementationLib
import com.flexcil.flexc.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")

                dependencies {
                    add("ksp", libs.findLibrary("hilt.compiler").get())
                }

                withPlugin("com.android.base") {
                    apply("dagger.hilt.android.plugin")

                    dependencies {
                        implementationLib("hilt.android")
                    }
                }
            }
        }
    }
}