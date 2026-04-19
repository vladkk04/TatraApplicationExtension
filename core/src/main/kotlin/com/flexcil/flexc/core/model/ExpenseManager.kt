package com.flexcil.flexc.core.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseManager @Inject constructor() {
    private val _expenses = MutableStateFlow<List<Expense>>(
        listOf(
            Expense(
                title = "BOLT",
                subtitle = "Daniil Dryzhov",
                avatars = 3,
                checkedAvatarIndex = null,
                isFirstAvatarEmpty = true,
                amountValue = "30.00",
                currency = "EUR",
                buttonState = "PAY_NOW",
                isCreatedByMe = false
            ),
            Expense(
                title = "The bill for yesterday’s party at the restaurant",
                subtitle = "Vladyslav Dorosh",
                avatars = 3,
                checkedAvatarIndex = 1,
                isFirstAvatarEmpty = false,
                amountValue = "120.00",
                currency = "EUR",
                buttonState = "PAID_CHECKED",
                isCreatedByMe = false
            )
        )
    )
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    fun addExpense(expense: Expense) {
        _expenses.value = listOf(expense) + _expenses.value
    }
}
