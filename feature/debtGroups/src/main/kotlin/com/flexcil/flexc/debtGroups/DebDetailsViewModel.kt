package com.flexcil.flexc.debtGroups

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebDetailsViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {
    fun navigateToPayment() {
        navigator.launchScreen(AppScreen.PaymentScreen)
    }
}