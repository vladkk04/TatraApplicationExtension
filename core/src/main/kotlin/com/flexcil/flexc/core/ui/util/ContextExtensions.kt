package com.flexcil.flexc.core.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import kotlin.also

fun Context.openAppSettings() = Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", packageName, null)
).also(::startActivity)