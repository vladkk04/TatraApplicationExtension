package com.flexcil.flexc.debtGroups.screen

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexcil.flexc.debtGroups.DebDetailsViewModel
import com.flexcil.flexc.core.navigation.LocalNavigator
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.model.BackgroundStyle
import com.flexcil.flexc.core.model.GroupItem
import com.flexcil.flexc.core.ui.component.IconPickerSheet
import kotlin.math.abs

private val mockGroups = listOf(
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

@Composable
fun MeDebGroupScreen(
    onQrScannerClick: () -> Unit,
    onQrCreatorClick: () -> Unit,
    viewModel: DebDetailsViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val balance by viewModel.groupBalance.collectAsState()
    var showIconPicker by remember { mutableStateOf(false) }
    var currentGroups by remember { mutableStateOf(mockGroups) }
    var groupToUpdate by remember { mutableStateOf<GroupItem?>(null) }

    // Use derived state for Party Debts to make it responsive
    val finalGroups = currentGroups.map { group ->
        if (group.title == "Party Debts") {
            group.copy(balance = balance.replace("+", "").replace(" EUR", "").toDoubleOrNull() ?: 0.0)
        } else group
    }

    if (showIconPicker) {
        IconPickerSheet(
            onDismissRequest = { showIconPicker = false },
            onIconSelected = { newIcon ->
                groupToUpdate?.let { target ->
                    currentGroups = currentGroups.map {
                        if (it.title == target.title) it.copy(icon = newIcon) else it
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Your groups",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Group List
        finalGroups.forEach { group ->
            GroupCard(
                group = group,
                onQrCreatorClick = onQrCreatorClick,
                onClick = {
                    navigator.launchScreen(AppScreen.DebDetails(groupName = group.title))
                },
                onIconClick = {
                    groupToUpdate = group
                    showIconPicker = true
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(80.dp)) // Padding for FAB
    }
}

@Composable
private fun GroupCard(
    group: GroupItem,
    onQrCreatorClick: () -> Unit,
    onClick: () -> Unit,
    onIconClick: () -> Unit
) {
    val isGradient = group.backgroundStyle != BackgroundStyle.DARK_SOLID
    val backgroundModifier = when (group.backgroundStyle) {
        BackgroundStyle.RED_GRADIENT -> Modifier.background(
            Brush.linearGradient(listOf(Color(0xFFE55D5D), Color(0xFF4A2B2B)))
        )
        BackgroundStyle.BLUE_GRADIENT -> Modifier.background(
            Brush.linearGradient(listOf(Color(0xFF2B88F0), Color(0xFF1C3A5A)))
        )
        BackgroundStyle.DARK_SOLID -> Modifier.background(MaterialTheme.colorScheme.surface)
    }

    val titleColor = if (isGradient) Color.White else MaterialTheme.colorScheme.onSurface
    val subtitleColor = if (isGradient) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(backgroundModifier)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable { onIconClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = group.icon,
                    contentDescription = group.title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.title,
                    color = titleColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = group.subtitle,
                    color = subtitleColor,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OverlappingAvatars(usersCount = group.usersCount)
            }


            // Balance
            val balanceColor = when {
                group.balance > 0 -> MaterialTheme.colorScheme.tertiary
                group.balance < 0 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            val balancePrefix = if (group.balance > 0) "+ " else if (group.balance < 0) "- " else ""
            val formattedBalance = "${balancePrefix}${abs(group.balance).toInt()} EUR"

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onQrCreatorClick,
                    modifier = Modifier
                        .offset(y = (-8).dp)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Show QR Code",
                        tint = Color(0xFF2B88F0), // A nice vibrant blue
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = formattedBalance,
                    color = balanceColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun OverlappingAvatars(usersCount: Int) {
    val avatarColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onSurfaceVariant,
        MaterialTheme.colorScheme.error,
        Color(0xFFD2BCA0) // Залишаємо пісочний колір як додатковий
    )
    Row {
        for (i in 0 until usersCount) {
            Box(
                modifier = Modifier
                    .offset(x = (-8 * i).dp) // Накладання одне на одне
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(avatarColors[i % avatarColors.size])
                    .border(1.5.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}