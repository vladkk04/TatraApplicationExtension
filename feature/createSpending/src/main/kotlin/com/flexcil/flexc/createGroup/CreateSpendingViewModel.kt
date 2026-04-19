package com.flexcil.flexc.createGroup

import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.model.Expense
import com.flexcil.flexc.core.model.ExpenseManager
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateSpendingViewModel @Inject constructor(
    private val navigator: Navigator,
    private val expenseManager: ExpenseManager
) : ViewModel() {

    fun addExpense(title: String, amount: String, debtorsCount: Int) {
        val newExpense = Expense(
            title = title,
            subtitle = "Danyil Yatluk",
            avatars = debtorsCount,
            checkedAvatarIndex = null,
            isFirstAvatarEmpty = true,
            amountValue = amount,
            currency = "EUR",
            buttonState = "PAY_NOW",
            isCreatedByMe = true
        )
        expenseManager.addExpense(newExpense)
        navigator.goBack()
    }

    fun goBack() {
        navigator.goBack()
    }
}
