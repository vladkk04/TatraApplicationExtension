package com.flexcil.flexc.convention

import org.gradle.api.JavaVersion

object AppConfig {
    val JAVA_VERSION = JavaVersion.VERSION_17
    const val COMPILE_SDK = 36
    const val MIN_SDK = 24
    const val TARGET_SDK = 36
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
    const val NAMESPACE: String = "com.flexcil.flexc"
    const val APPLICATION_ID: String = "com.flexcil.flexc"
}