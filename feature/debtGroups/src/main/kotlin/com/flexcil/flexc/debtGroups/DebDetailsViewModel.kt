package com.flexcil.flexc.debtGroups

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import com.flexcil.flexc.core.model.ExpenseManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel
class DebDetailsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val expenseManager: ExpenseManager
): ViewModel() {
    val expenses: StateFlow<List<com.flexcil.flexc.core.model.Expense>> = expenseManager.expenses

    val groupBalance: StateFlow<String> = expenses.combine(MutableStateFlow(0.0)) { currentExpenses, _ ->
        var totalBalance = 0.0
        currentExpenses.forEach { expense ->
            val amount = try {
                expense.amountValue.replace(",", ".").toDouble()
            } catch (e: Exception) {
                0.0
            }

            if (expense.isCreatedByMe) {
                // If I created it, others owe me (total - my portion)
                // Assuming I am also part of the split if I am not in debtors, 
                // but usually create spending means I paid and others owe me.
                // In our model, 'avatars' is total people.
                val perPerson = amount / expense.avatars
                totalBalance += (amount - perPerson)
            } else if (expense.buttonState == "PAY_NOW") {
                // If I need to pay, it's a debt
                val perPerson = amount / expense.avatars
                totalBalance -= perPerson
            }
        }
        val prefix = if (totalBalance > 0) "+" else if (totalBalance < 0) "" else ""
        String.format(Locale.US, "%s%.0f EUR", prefix, totalBalance)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0 EUR")

    fun navigateToPayment(beneficiaryName: String? = "Daniil Dryzov", amount: String? = "10,00") {
        navigator.launchScreen(AppScreen.PaymentScreen(
            beneficiaryName = beneficiaryName,
            amount = amount,
            iban = "SK12 1100 0000 0012 3456 7890",
            payerName = "Vladyslav Klymiuk",
            payerIban = "SK88 1100 0000 0098 7654 3210",
            payerBalance = "2,47 EUR"
        ))
    }

    fun navigateToCreateSpendingScreen() {
        navigator.launchScreen(AppScreen.CreateSpendingScreen)
    }

    fun goBack() {
        navigator.goBack()
    }
}