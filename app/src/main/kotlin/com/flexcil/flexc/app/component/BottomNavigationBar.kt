package com.flexcil.flexc.app.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flexcil.flexc.app.R

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onSharedClick: () -> Unit,
    onPaymentClick: () -> Unit,
    onTransactionClick: () -> Unit
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Єдине джерело правди для визначення активної вкладки
    var selectedTab by remember { mutableStateOf("Home") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(Modifier.fillMaxWidth())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    selected = selectedTab == "Home",
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    onClick = {
                        selectedTab = "Home"
                        onHomeClick()
                    }
                )

                BottomNavItem(
                    icon = Icons.Default.SyncAlt,
                    label = "Transactions",
                    selected = selectedTab == "Transactions",
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    onClick = {
                        selectedTab = "Transactions"
                        onTransactionClick()
                    }
                )
            }

            CenterPaymentItem(
                icon = Icons.Outlined.CreditCard,
                label = "Payment",
                selected = selectedTab == "Payment", // Передаємо стан
                selectedColor = selectedColor,
                unselectedColor = unselectedColor,
                gradientTopColor = selectedColor,
                onClick = {
                    selectedTab = "Payment"
                    onPaymentClick()
                }
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    painter = painterResource(R.drawable.share), // Переконайтеся, що імпорт R правильний
                    label = "Shared",
                    selected = selectedTab == "Shared",
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    onClick = {
                        selectedTab = "Shared"
                        onSharedClick()
                    }
                )

                BottomNavItem(
                    icon = Icons.Default.Menu,
                    label = "More",
                    selected = selectedTab == "More",
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    onClick = {
                        selectedTab = "More"
                        // Додайте onMoreClick сюди, якщо потрібно
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val color = if (selected) selectedColor else unselectedColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BottomNavItem(
    painter: Painter,
    label: String,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val color = if (selected) selectedColor else unselectedColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CenterPaymentItem(
    icon: ImageVector,
    label: String,
    selected: Boolean, // Додано параметр
    selectedColor: Color, // Додано параметр
    unselectedColor: Color, // Додано параметр
    gradientTopColor: Color,
    onClick: () -> Unit
) {
    val borderBrush = Brush.verticalGradient(
        colors = listOf(gradientTopColor, Color.Transparent),
        startY = 0f,
        endY = 150f
    )

    // Визначаємо колір іконки та тексту залежно від вибраного стану
    val color = if (selected) selectedColor else unselectedColor

    Box(
        modifier = Modifier
            .size(76.dp)
            .border(width = 1.5.dp, brush = borderBrush, shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}