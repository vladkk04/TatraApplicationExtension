package com.flexcil.flexc.core.model

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
}
