package com.flexcil.flexc.core.model

data class Expense(
    val title: String,
    val subtitle: String?,
    val avatars: Int,
    val checkedAvatarIndex: Int? = null,
    val isFirstAvatarEmpty: Boolean = false,
    val amountValue: String,
    val currency: String,
    val buttonState: String,
    val isCreatedByMe: Boolean = false
)
