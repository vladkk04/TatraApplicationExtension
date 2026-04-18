package com.flexcil.flexc.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")


fun Project.autoNamespaceModule(): String =
    AppConfig.NAMESPACE + project.path.replace(":", ".")

fun Project.implementation(dependencyNotation: Any) = dependencies.add("implementation", dependencyNotation)
fun Project.implementationLib(libName: String) = implementation(libs.findLibrary(libName).get())



