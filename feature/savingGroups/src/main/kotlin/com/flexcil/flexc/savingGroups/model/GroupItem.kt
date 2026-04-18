package com.flexcil.flexc.savingGroups.model

import androidx.compose.ui.graphics.vector.ImageVector

data class GroupItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val usersCount: Int,
    val balance: Double,
    val backgroundStyle: BackgroundStyle
)