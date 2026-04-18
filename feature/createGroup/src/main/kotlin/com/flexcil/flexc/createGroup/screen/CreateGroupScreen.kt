package com.flexcil.flexc.createGroup.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class GroupType {
    DEBT_GROUP, SAVINGS_ACCOUNT
}

@Composable
fun CreateGroupScreen() {
    var selectedGroupType by remember { mutableStateOf(GroupType.DEBT_GROUP) }
    var groupName by remember { mutableStateOf("") }
    var finalGoal by remember { mutableStateOf("") }

    // Захардкоджене значення для валюти згідно дизайну
    val currency = "EUR"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // --- Header (Заголовок) ---
        // Видаліть цей блок, якщо будете використовувати окремий TopBar
        Text(
            text = "New Group",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // --- Radio Buttons (Вибір типу групи) ---
        RadioOption(
            text = "Debt Group",
            selected = selectedGroupType == GroupType.DEBT_GROUP,
            onClick = { selectedGroupType = GroupType.DEBT_GROUP }
        )

        RadioOption(
            text = "Savings Account",
            selected = selectedGroupType == GroupType.SAVINGS_ACCOUNT,
            onClick = { selectedGroupType = GroupType.SAVINGS_ACCOUNT }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Тонка лінія розділювач
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Форма вводу ---
        CustomInputField(
            label = "Group Name",
            value = groupName,
            onValueChange = { groupName = it },
            placeholder = "Enter group name..."
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Поле вибору валюти (імітація Dropdown)
        CustomInputField(
            label = "Currency",
            value = currency,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Select Currency",
                    tint = MaterialTheme.colorScheme.primary // Синя стрілочка як на дизайні
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomInputField(
            label = "Final Goal (Optional)",
            value = finalGoal,
            onValueChange = { finalGoal = it },
            placeholder = "Optional: e.g., 10,000 EUR"
        )

        // Використовуємо Spacer з weight(1f) щоб відштовхнути кнопку в самий низ
        Spacer(modifier = Modifier.weight(1f))

        // --- Кнопка створення ---
        Button(
            onClick = { /* Handle Create Group */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Create Group",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp), // Падінг для зручного натискання
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            },
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp), // Закруглені кути як на дизайні
            colors = OutlinedTextFieldDefaults.colors(
                // Налаштування кольорів рамок
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                // Робимо фон прозорим
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                // Кольори тексту
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            singleLine = true
        )
    }
}