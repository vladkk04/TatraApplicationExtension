package com.flexcil.flexc.savingGroups

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flexcil.flexc.core.model.BackgroundStyle
import com.flexcil.flexc.core.model.GroupItem

val mockGroups = listOf(
    GroupItem(
        title = "Party",
        subtitle = "Вечірка",
        icon = Icons.Default.Group,
        usersCount = 4,
        balance = 4400.0,
        backgroundStyle = BackgroundStyle.DARK_SOLID
    ),
    GroupItem(
        title = "Food",
        subtitle = "Їжа",
        icon = Icons.Default.Restaurant,
        usersCount = 3,
        balance = 100.0,
        backgroundStyle = BackgroundStyle.DARK_SOLID
    ),
    GroupItem(
        title = "Weekend Trip",
        subtitle = "Поїздка на вихідні",
        icon = Icons.Default.Backpack,
        usersCount = 3,
        balance = 0.0,
        backgroundStyle = BackgroundStyle.DARK_SOLID
    ),
    GroupItem(
        title = "Lunch",
        subtitle = "Обід",
        icon = Icons.Default.Restaurant,
        usersCount = 0,
        balance = 0.0,
        backgroundStyle = BackgroundStyle.DARK_SOLID
    )
)

@Composable
fun SavingGroupsScreen(
    viewModel: SavingGroupsViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        // --- QR Scanner Header ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { /* Logic for QR Scanner if needed */ },
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan QR",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Scan QR code to\njoin a group",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Your groups",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Groups List ---
        mockGroups.forEach { group ->
            GroupCard(
                group = group,
                onClick = {
                    // Navigate to details specifically for the Party group
                    if (group.title == "Party") {
                        viewModel.navigateToSavingGroupDetails()
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Extra spacer to ensure content isn't hidden by the BottomBar
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun GroupCard(
    group: GroupItem,
    onClick: () -> Unit
) {
    val backgroundModifier = when (group.backgroundStyle) {
        BackgroundStyle.RED_GRADIENT -> Modifier.background(
            Brush.linearGradient(listOf(Color(0xFFE55D5D), Color(0xFF4A2B2B)))
        )
        BackgroundStyle.BLUE_GRADIENT -> Modifier.background(
            Brush.linearGradient(listOf(Color(0xFF2B88F0), Color(0xFF1C3A5A)))
        )
        BackgroundStyle.DARK_SOLID -> Modifier.background(MaterialTheme.colorScheme.surface)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(backgroundModifier)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.3f)),
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

            // Text Information
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = group.subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OverlappingAvatars(usersCount = group.usersCount)
            }

            // Balance - Formatting: No +/- prefixes
            val balanceColor = when {
                group.balance > 0 -> MaterialTheme.colorScheme.tertiary
                group.balance < 0 -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Text(
                text = "${group.balance.toInt()} EUR",
                color = balanceColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun OverlappingAvatars(usersCount: Int) {
    val avatarColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onSurfaceVariant,
        MaterialTheme.colorScheme.error,
        Color(0xFFD2BCA0)
    )
    Row {
        for (i in 0 until usersCount) {
            Box(
                modifier = Modifier
                    .offset(x = (-8 * i).dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(avatarColors[i % avatarColors.size])
                    .border(1.5.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}