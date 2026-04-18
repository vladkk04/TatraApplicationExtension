package com.flexcil.flexc.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen: NavKey {

    @Serializable
    data object InitialScreen : AppScreen

    @Serializable
    data object MenuScreen : AppScreen

    @Serializable
    data object GameScreen : AppScreen
}
