package com.flexcil.flexc.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen: NavKey {

    @Serializable
    data object InitialScreen : AppScreen

    @Serializable
    data object SharedScreen : AppScreen

    @Serializable
    data object QrScanner: AppScreen

    @Serializable
    data object NewGroup: AppScreen

    @Serializable
    data object CreateGroupScreen: AppScreen

    @Serializable
    data object CreateSpendingScreen: AppScreen


    @Serializable
    data object DebDetails: AppScreen

    @Serializable
    data object SavingGroupsDetails: AppScreen

    @Serializable
    data object PaymentScreen: AppScreen

    @Serializable
    data object TransactionScreen: AppScreen
}
