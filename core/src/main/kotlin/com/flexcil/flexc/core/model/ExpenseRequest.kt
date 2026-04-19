package com.flexcil.flexc.core.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.runtime.mutableStateListOf

data class ExpenseRequest(
    val id: String,
    val requesterName: String,
    val requesterInitials: String,
    val amount: Double,
    var approvedCount: Int = 0,
    val totalNeeded: Int = 3,
    var isSelfApproved: Boolean = false
)

object GlobalMockData {
    val pendingRequests = mutableStateListOf<ExpenseRequest>()

    val debtGroups = mutableStateListOf<GroupItem>(
        GroupItem(
            title = "Party Debts",
            subtitle = "Part Description",
            icon = Icons.Default.Group,
            usersCount = 4,
            balance = -10.0,
            backgroundStyle = BackgroundStyle.DARK_SOLID

        ),
        GroupItem(
            title = "Food Sharing",
            subtitle = "Food Description",
            icon = Icons.Default.Restaurant,
            usersCount = 3,
            balance = -30.0,
            backgroundStyle = BackgroundStyle.DARK_SOLID
        )
    )

    val savingGroups = mutableStateListOf<GroupItem>(
        GroupItem(
            title = "Saving for Party",
            subtitle = "Saving for party description",
            icon = Icons.Default.Group,
            usersCount = 4,
            balance = 4400.0,
            backgroundStyle = BackgroundStyle.DARK_SOLID
        ),
        GroupItem(
            title = "Grocery Fund",
            subtitle = "Grocery description",
            icon = Icons.Default.Restaurant,
            usersCount = 3,
            balance = 100.0,
            backgroundStyle = BackgroundStyle.DARK_SOLID
        )
    )
}
