package com.flexcil.flexc.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

interface Navigator {

    fun launchScreen(screen: AppScreen)

    fun goBack()

}

val LocalNavigator: ProvidableCompositionLocal<Navigator> = staticCompositionLocalOf {
    error("No Navigator provided")
}
