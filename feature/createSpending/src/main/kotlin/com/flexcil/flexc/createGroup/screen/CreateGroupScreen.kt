package com.flexcil.flexc.createGroup.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Custom Colors ---
private val AvatarRed = Color(0xFF6B1E2C)
private val AvatarOlive = Color(0xFF4C5D23)
private val AvatarPurple = Color(0xFF2E235D)
private val AvatarRose = Color(0xFFC08985)
private val DarkCardBorder = Color(0xFF2F3036)

// --- Data Models ---
enum class GroupType {
    DEBT_GROUP, SAVINGS_ACCOUNT
}

data class Member(
    val id: String,
    val name: String,
    val initials: String,
    val detail: String,
    val avatarColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen() {
    var selectedGroupType by remember { mutableStateOf(GroupType.DEBT_GROUP) }
    var groupName by remember { mutableStateOf("") }

    // Currency Dropdown State
    var currencyExpanded by remember { mutableStateOf(false) }
    val currencies = listOf("EUR", "CZK", "USD", "GBP", "PLN", "HUF")
    var selectedCurrency by remember { mutableStateOf(currencies[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // --- Scrollable Form Area ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
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

            // Segmented Control (Вибір типу групи)
            val options = listOf("Debt Group", "Savings Account")
            val selectedIndex = if (selectedGroupType == GroupType.DEBT_GROUP) 0 else 1

            SegmentedControl(
                options = options,
                selectedIndex = selectedIndex,
                onOptionSelected = { index ->
                    selectedGroupType = if (index == 0) GroupType.DEBT_GROUP else GroupType.SAVINGS_ACCOUNT
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Тонка лінія розділювач
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поле: Group Name
            CustomInputField(
                label = "Group Name",
                value = groupName,
                onValueChange = { groupName = it },
                placeholder = "Enter group name..."
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Поле: Currency (Dropdown)
            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = !currencyExpanded }
            ) {
                CustomInputField(
                    label = "Currency",
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = currencyExpanded
                        )
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = currencyExpanded,
                    onDismissRequest = { currencyExpanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(text = currency, color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                selectedCurrency = currency
                                currencyExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Список вибору учасників (Now includes the Beneficiary button)
            MemberSelectionList()

            Spacer(modifier = Modifier.height(24.dp)) // Extra padding at the bottom of the scroll
        }

        // --- Кнопка створення (Pinned to bottom) ---
        Button(
            onClick = { /* Handle Create Group */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(top = 8.dp, bottom = 8.dp),
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

// --- Reusable Components Below ---

@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    val containerColor = Color(0xFF141416)
    val selectedColor = Color(0xFF2F3038)
    val textColorSelected = Color(0xFFFFFFFF)
    val textColorUnselected = Color(0xFF8B8B90)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(
                color = containerColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, text ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) selectedColor else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onOptionSelected(index) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = if (isSelected) textColorSelected else textColorUnselected,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CustomInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
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
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            singleLine = true
        )
    }
}

@Composable
fun MemberSelectionList(modifier: Modifier = Modifier) {
    val availableMembers = remember {
        listOf(
            Member("1", "Vladyslav Dorosh", "VD", "+421 900 123 456", AvatarRed),
            Member("2", "Vladyslav Klymiuk", "VK", "+421 911 654 321", AvatarOlive),
            Member("3", "Daniil Dryzhov", "DD", "+421 944 111 222", AvatarPurple),
            Member("4", "Danyil Yatluk", "DY", "+421 950 999 888", AvatarRose)
        )
    }

    var selectedMemberIds by remember { mutableStateOf(setOf<String>()) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Add Members",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // --- NEW: Choose Beneficiary Button ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp)) // Added border here
                .background(Color(0xFF141416))
                .clickable { /* Handle opening beneficiary picker */ }
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Choose beneficiary",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Rounded.Contacts,
                contentDescription = "Choose Beneficiary",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Existing Members List ---
        availableMembers.forEach { member ->
            val isSelected = selectedMemberIds.contains(member.id)

            SelectableMemberItem(
                member = member,
                isSelected = isSelected,
                onClick = {
                    selectedMemberIds = if (isSelected) {
                        selectedMemberIds - member.id
                    } else {
                        selectedMemberIds + member.id
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SelectableMemberItem(
    member: Member,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else DarkCardBorder
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(member.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.initials,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name and Detail
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = member.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = member.detail,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        // Selection Indicator
        Icon(
            imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = if (isSelected) "Selected" else "Unselected",
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}