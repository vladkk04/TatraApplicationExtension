package com.flexcil.flexc.app

import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {

    fun navigateHome() {
        navigator.launchScreen(AppScreen.InitialScreen)
    }

    fun navigateShared() {
        navigator.launchScreen(AppScreen.SharedScreen)
    }

    fun navigateTransaction() {
        navigator.launchScreen(AppScreen.TransactionScreen)
    }

    fun navigatePayment() {
        navigator.launchScreen(AppScreen.PaymentScreen())
    }

    fun navigateBack() {
        navigator.goBack()
    }

}