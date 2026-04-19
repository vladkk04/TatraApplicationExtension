package com.flexcil.flexc.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen: NavKey {

    @Serializable
    data object InitialScreen : AppScreen

    @Serializable
    data object PendingScreen : AppScreen

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
    data class DebDetails(val groupName: String? = null) : AppScreen

    @Serializable
    data object SavingGroupsDetails: AppScreen

    @Serializable
    data object QrCreator: AppScreen

    @Serializable
    data class PaymentScreen(
        val beneficiaryName: String? = null,
        val iban: String? = null,
        val amount: String? = null,
        val information: String? = null,
        val payerName: String? = null,
        val payerIban: String? = null,
        val payerBalance: String? = null
    ) : AppScreen

    @Serializable
    data class RequestExpenseScreen(
        val beneficiaryName: String? = null,
        val iban: String? = null,
        val amount: String? = null,
        val information: String? = null,
        val payerName: String? = null,
        val payerIban: String? = null,
        val payerBalance: String? = null
    ) : AppScreen

    @Serializable
    data object TransactionScreen: AppScreen
}
