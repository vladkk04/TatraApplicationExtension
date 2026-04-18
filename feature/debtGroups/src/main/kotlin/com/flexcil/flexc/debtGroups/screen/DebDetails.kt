package com.flexcil.flexc.debtGroups.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DepDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- Картка з розділенням витрат ---
        ExpenseGroupCard()
    }
}

@Composable
private fun ExpenseGroupCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface // Темно-сірий фон картки
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            // 1. First Item
            ExpenseSplitRow(
                title = "Sarah Miller",
                subtitle = "Paid",
                avatars = 3,
                checkedAvatarIndex = 1, // Зелена галочка на другій аватарці
                isFirstAvatarEmpty = false,
                amountValue = "30.00",
                currency = "EUR",
                buttonState = ButtonState.PAID_CHECKED
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 2. Second Item
            ExpenseSplitRow(
                title = "BOLD",
                subtitle = "Created by Danyil Yatluk",
                avatars = 3,
                checkedAvatarIndex = null,
                isFirstAvatarEmpty = true, // Перша аватарка - пустий сірий круг
                amountValue = "30.00",
                currency = "EUR",
                buttonState = ButtonState.PAY_NOW
            )
        }
    }
}

@Composable
private fun ExpenseSplitRow(
    title: String,
    subtitle: String?,
    avatars: Int,
    checkedAvatarIndex: Int? = null,
    isFirstAvatarEmpty: Boolean = false,
    amountValue: String,
    currency: String,
    buttonState: ButtonState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title & Subtitle (займає весь доступний простір зліва)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp) // Додаємо невеликий відступ від аватарок
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1, // Забороняємо переніс на новий рядок
                overflow = TextOverflow.Ellipsis // Додаємо "..." якщо не влазить
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1, // Забороняємо переніс на новий рядок
                    overflow = TextOverflow.Ellipsis // Додаємо "..." якщо не влазить
                )
            }
        }

        // Avatars
        CustomOverlappingAvatars(
            count = avatars,
            checkedIndex = checkedAvatarIndex,
            firstEmpty = isFirstAvatarEmpty
        )

        Spacer(modifier = Modifier.width(10.dp)) // Зменшив відступ, щоб дати тексту більше місця

        // Amount (Цифри великі білі, валюта менша сіра)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = amountValue,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = currency,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 1.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp)) // Зменшив відступ

        // Action Button
        ActionButton(state = buttonState)
    }
}

@Composable
private fun CustomOverlappingAvatars(
    count: Int,
    checkedIndex: Int? = null,
    firstEmpty: Boolean = false
) {
    // Кольори-заглушки для аватарок
    val avatarColors = listOf(
        Color(0xFF8B8D98), Color(0xFFD2BCA0), Color(0xFF2B88F0)
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .offset(x = if (i > 0) (-8 * i).dp else 0.dp) // Накладання
            ) {
                // Avatar Circle
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (firstEmpty && i == 0) Color(0xFFE0E0E0) else avatarColors[i % avatarColors.size])
                        .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape), // Рамка кольору фону картки
                    contentAlignment = Alignment.Center
                ) {
                    // Малюємо іконку людини, якщо це не пустий круг
                    if (!(firstEmpty && i == 0)) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Green Checkmark Badge
                if (checkedIndex == i) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00D67D)) // Зелений колір
                            .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }
            }
        }
    }
}

enum class ButtonState {
    PAID_CHECKED, PAY_NOW
}

@Composable
private fun ActionButton(state: ButtonState) {
    val containerColor = when (state) {
        ButtonState.PAY_NOW -> MaterialTheme.colorScheme.primary // Синя кнопка
        ButtonState.PAID_CHECKED -> MaterialTheme.colorScheme.surfaceVariant // Сіра кнопка
    }

    val contentColor = when (state) {
        ButtonState.PAY_NOW -> MaterialTheme.colorScheme.onPrimary
        ButtonState.PAID_CHECKED -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Button(
        onClick = { /* Handle action */ },
        shape = RoundedCornerShape(8.dp), // Квадратніші кути як на дизайні
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        // Фіксуємо ширину і висоту, щоб кнопки були однаковими і акуратними
        modifier = Modifier
            .height(34.dp)
            .width(96.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (state == ButtonState.PAY_NOW) "Pay Now" else "Paid",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            if (state == ButtonState.PAID_CHECKED) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, // Галочка в тон тексту
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}