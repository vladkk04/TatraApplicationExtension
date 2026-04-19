package com.flexcil.flexc.debtGroups

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import com.flexcil.flexc.core.model.ExpenseManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DebDetailsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val expenseManager: ExpenseManager
): ViewModel() {
    val expenses: StateFlow<List<com.flexcil.flexc.core.model.Expense>> = expenseManager.expenses

    fun navigateToPayment() {
        navigator.launchScreen(AppScreen.PaymentScreen)
    }

    fun navigateToCreateSpendingScreen() {
        navigator.launchScreen(AppScreen.CreateSpendingScreen)
    }

    fun goBack() {
        navigator.goBack()
    }
}