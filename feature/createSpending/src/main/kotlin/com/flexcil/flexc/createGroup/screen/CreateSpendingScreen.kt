package com.flexcil.flexc.createGroup.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

// --- Shared Custom Colors ---
private val AvatarRed = Color(0xFF6B1E2C)
private val AvatarOlive = Color(0xFF4C5D23)
private val AvatarPurple = Color(0xFF2E235D)
private val AvatarRose = Color(0xFFC08985)
private val DarkCardBorder = Color(0xFF2F3036)
private val InputBackground = Color(0xFF141416)
private val TatraBlue = Color(0xFF3B82F6)
private val ExpenseRed = Color(0xFFE53935) // Red color for expense amount

// --- Data Models ---
data class GroupMember(
    val id: String,
    val name: String,
    val initials: String,
    val detail: String,
    val avatarColor: Color
)

data class Expense(
    val name: String,
    val amount: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSpendingScreen() {
    // Simulated members
    val availableGroupMembers = remember {
        listOf(
            GroupMember("1", "Danyil Yatluk", "DY", "SK30 **** **** **** 3664", AvatarRose),
            GroupMember("2", "Vladyslav Dorosh", "VD", "SK30 **** **** **** 3664", AvatarRed),
            GroupMember("3", "Vladyslav Klymiuk", "VK", "SK30 **** **** **** 3664", AvatarOlive)
        )
    }

    // States
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedDebtorIds by remember { mutableStateOf(setOf<String>()) }

    // NEW: States for Manual Expense
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    var showManualExpenseModal by remember { mutableStateOf(false) }

    // Bottom Sheet Logic (Debtors)
    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            DebtorsBottomSheetContent(
                availableMembers = availableGroupMembers,
                selectedIds = selectedDebtorIds,
                onSelectionChange = { selectedDebtorIds = it }
            )
        }
    }

    // NEW: Dialog Logic (Manual Expense)
    if (showManualExpenseModal) {
        AddManualExpenseDialog(
            initialName = selectedExpense?.name ?: "",
            initialAmount = selectedExpense?.amount ?: "",
            onDismiss = { showManualExpenseModal = false },
            onSave = { name, amount ->
                selectedExpense = Expense(name, amount)
                showManualExpenseModal = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Top Headers ---
        Text(
            text = "Adding New SubGroup",
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        // --- Expense Section (Toggle based on state) ---
        if (selectedExpense == null) {
            // State 1: Choose how to add
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(InputBackground)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Add spendings",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { showManualExpenseModal = true }, // Opens Modal
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TatraBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Add Manually", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { /* Add from transactions Action */ },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TatraBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("From transactions", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        } else {
            // State 2: Expense is selected
            Text(
                text = "Selected Expense",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .background(InputBackground)
                    .clickable { showManualExpenseModal = true } // Allows re-editing
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minus Icon indicator
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = null,
                    tint = ExpenseRed,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedExpense!!.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Made by You", // Placeholder for creator
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                // Amount
                Text(
                    text = "${selectedExpense!!.amount} EUR",
                    color = ExpenseRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Helpful text to show they can change it
            Text(
                text = "Tap the card to edit",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Add from transactions Action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TatraBlue,
                    contentColor = Color.White
                )
            ) {
                Text("From transactions", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Debtors Section ---
        Text(
            text = "Debtors",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                .background(InputBackground)
                .clickable { showBottomSheet = true }
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (selectedDebtorIds.isEmpty()) "Add debtors" else "${selectedDebtorIds.size} debtors selected",
                color = if (selectedDebtorIds.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Rounded.Contacts,
                contentDescription = "Add Debtors",
                tint = TatraBlue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val selectedMembersList = availableGroupMembers.filter { selectedDebtorIds.contains(it.id) }
        selectedMembersList.forEach { debtor ->
            DebtorItemStyle(member = debtor)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(40.dp))

        // --- Apply Button ---
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = { /* Apply Action */ },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TatraBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Confirm", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// --- NEW: Add Manual Expense Dialog Modal ---
@Composable
fun AddManualExpenseDialog(
    initialName: String,
    initialAmount: String,
    onDismiss: () -> Unit,
    onSave: (name: String, amount: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var amount by remember { mutableStateOf(initialAmount) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text(
                text = "Add Manual Expense",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Expense Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Expense Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DarkCardBorder,
                    focusedBorderColor = TatraBlue,
                    unfocusedContainerColor = InputBackground,
                    focusedContainerColor = InputBackground
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*([.,]\\d*)?\$"))) {
                        amount = newValue
                    }
                },
                label = { Text("Amount (EUR)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DarkCardBorder,
                    focusedBorderColor = TatraBlue,
                    unfocusedContainerColor = InputBackground,
                    focusedContainerColor = InputBackground
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (name.isNotBlank() && amount.isNotBlank()) {
                            onSave(name, amount)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TatraBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ... [Keep DebtorsBottomSheetContent, DebtorItemStyle, and SelectableMemberItem exactly as they were] ...

// Note: Ensure the bottom sheet and item composables from the previous step are pasted below this point!

@Composable
fun DebtorsBottomSheetContent(
    availableMembers: List<GroupMember>,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredMembers = availableMembers.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.detail.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search members...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = DarkCardBorder,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = InputBackground,
                focusedContainerColor = InputBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add everyone",
            color = TatraBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable {
                    val newSelection = selectedIds + filteredMembers.map { it.id }.toSet()
                    onSelectionChange(newSelection)
                }
                .padding(vertical = 8.dp, horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredMembers) { member ->
                val isSelected = selectedIds.contains(member.id)
                SelectableMemberItem(
                    member = member,
                    isSelected = isSelected,
                    onClick = {
                        val newSet = if (isSelected) selectedIds - member.id else selectedIds + member.id
                        onSelectionChange(newSet)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DebtorItemStyle(member: GroupMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
            .background(InputBackground)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(member.avatarColor), contentAlignment = Alignment.Center) {
            Text(member.initials, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(member.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(member.detail, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SelectableMemberItem(member: GroupMember, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else DarkCardBorder
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .background(InputBackground)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(member.avatarColor), contentAlignment = Alignment.Center) {
            Text(member.initials, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(member.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(member.detail, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        Icon(if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked, contentDescription = null, tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
    }
}