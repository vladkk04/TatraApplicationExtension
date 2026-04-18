package com.flexcil.flexc.convention

import com.android.build.gradle.AbstractAppExtension
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.android.build.gradle.internal.tasks.FinalizeBundleTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File

fun Project.configureBuildAppOutputs() {
    extensions.configure<AbstractAppExtension> {
        applicationVariants.configureEach {
            val outputName = "${getAppNameFromStringsXml(this@configureBuildAppOutputs)}_v_${versionName}_${AppConfig.APPLICATION_ID}"

            outputs.forEach {
                (it as? ApkVariantOutputImpl)?.outputFileName = "$outputName.apk"
            }

            tasks.named(
                "sign${name.replaceFirstChar(Char::uppercase)}Bundle",
                FinalizeBundleTask::class.java
            ) {
                finalBundleFile.set(
                    File(
                        finalBundleFile.asFile.get().parentFile,
                        "$outputName.aab"
                    )
                )
            }
        }
    }
}

fun getAppNameFromStringsXml(project: Project): String {
    with(project) {
        val stringsXml = File(projectDir, "src/main/res/values/strings.xml").takeIf {
            it.exists()
        } ?: return rootProject.name

        return (Regex("<string name=\"app_name\">(.*?)</string>")
            .find(stringsXml.readText())?.groupValues?.get(1) ?: project.rootProject.name)
            .replace(Regex("[/\\\\:*?\"<>|]"), "")
    }
}

