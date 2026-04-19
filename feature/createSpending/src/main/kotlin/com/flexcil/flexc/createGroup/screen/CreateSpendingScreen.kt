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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flexcil.flexc.createGroup.CreateSpendingViewModel

// --- Standard App Colors ---
private val AppBackground = Color(0xFF14151A)
private val CardBackground = Color(0xFF232429)
private val InputBackground = Color(0xFF1C1D22)
private val PrimaryBlue = Color(0xFF2B88F0)
private val TextGray = Color(0xFF8B8D98)
private val DarkCardBorder = Color(0xFF2F3036)
private val ExpenseRed = Color(0xFFE55D5D)

// --- Avatar Colors ---
private val AvatarRed = Color(0xFF6B1E2C)
private val AvatarOlive = Color(0xFF4C5D23)
private val AvatarPurple = Color(0xFF2E235D)
private val AvatarRose = Color(0xFFC08985)

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
    val viewModel = hiltViewModel<CreateSpendingViewModel>()

    val availableGroupMembers = remember {
        listOf(
            GroupMember("1", "Danyil Yatluk", "DY", "SK30 **** **** **** 3664", AvatarRose),
            GroupMember("2", "Vladyslav Dorosh", "VD", "SK30 **** **** **** 3664", AvatarRed),
            GroupMember("3", "Vladyslav Klymiuk", "VK", "SK30 **** **** **** 3664", AvatarOlive)
        )
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedDebtorIds by remember { mutableStateOf(setOf<String>()) }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }

    var showManualExpenseModal by remember { mutableStateOf(false) }
    var showTransactionPicker by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = AppBackground
        ) {
            DebtorsBottomSheetContent(
                availableMembers = availableGroupMembers,
                selectedIds = selectedDebtorIds,
                onSelectionChange = { selectedDebtorIds = it }
            )
        }
    }

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

    // Transaction Picker Bottom Sheet
    if (showTransactionPicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showTransactionPicker = false },
            sheetState = sheetState,
            containerColor = AppBackground
        ) {
            TransactionPickerContent(
                onTransactionSelected = { transaction ->
                    selectedExpense = Expense(
                        name = transaction.title,
                        amount = transaction.amount
                    )
                    showTransactionPicker = false
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // --- Main Scrollable Content ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Adding New SubGroup",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- Section 1: Expense Details ---
            if (selectedExpense == null) {
                Text(
                    text = "Expense details",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Option Cards
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ActionCard(
                        title = "Manual entry",
                        icon = Icons.Outlined.Edit,
                        modifier = Modifier.weight(1f),
                        onClick = { showManualExpenseModal = true }
                    )
                    ActionCard(
                        title = "From transactions",
                        icon = Icons.Outlined.ReceiptLong,
                        modifier = Modifier.weight(1f),
                        onClick = { showTransactionPicker = true }
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Expense",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear",
                        tint = PrimaryBlue,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { selectedExpense = null }
                    )
                }

                // Selected Expense Card
                Surface(
                    modifier = Modifier.fillMaxWidth().border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = CardBackground,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showManualExpenseModal = true }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedExpense!!.name,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Made by you",
                                color = TextGray,
                                fontSize = 13.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = selectedExpense!!.amount,
                                    color = ExpenseRed,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "EUR",
                                    color = ExpenseRed,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Edit",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit", color = PrimaryBlue, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Section 2: Split With (Debtors) ---
            Text(
                text = "Split with",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Add People Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .background(InputBackground)
                    .clickable { showBottomSheet = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.PersonAdd, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = if (selectedDebtorIds.isEmpty()) "Add people" else "${selectedDebtorIds.size} people selected",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Choose who was involved",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Members List
            val selectedMembersList = availableGroupMembers.filter { selectedDebtorIds.contains(it.id) }
            if (selectedMembersList.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CardBackground
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        selectedMembersList.forEachIndexed { index, debtor ->
                            DebtorItemStyle(member = debtor)
                            if (index < selectedMembersList.lastIndex) {
                                HorizontalDivider(
                                    color = DarkCardBorder,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // Safe space for bottom sticky button
        }

        // --- Sticky Bottom Button ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(AppBackground)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = {
                    selectedExpense?.let { expense ->
                        viewModel.addExpense(
                            title = expense.name,
                            amount = expense.amount,
                            debtorsCount = selectedDebtorIds.size
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    disabledContainerColor = DarkCardBorder
                ),
                enabled = selectedExpense != null && selectedDebtorIds.isNotEmpty()
            ) {
                Text("Confirm", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

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
                .clip(RoundedCornerShape(20.dp))
                .background(CardBackground)
                .padding(24.dp)
        ) {
            Text(
                text = "Add manual expense",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("What was it for?", color = TextGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DarkCardBorder,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedContainerColor = InputBackground,
                    focusedContainerColor = InputBackground,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*([.,]\\d*)?\$"))) {
                        amount = newValue
                    }
                },
                label = { Text("Amount (EUR)", color = TextGray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = DarkCardBorder,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedContainerColor = InputBackground,
                    focusedContainerColor = InputBackground,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TextGray, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (name.isNotBlank() && amount.isNotBlank()) onSave(name, amount)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

// --- Transaction Picker Components ---
@Composable
fun TransactionPickerContent(onTransactionSelected: (TransactionData) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Text(
            text = "Select from transactions",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            mockTransactionGroups.forEach { group ->
                item {
                    Text(
                        text = group.date,
                        color = TextGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }

                items(group.transactions) { transaction ->
                    TransactionPickerItem(transaction = transaction, onClick = { onTransactionSelected(transaction) })
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(DarkCardBorder.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionPickerItem(transaction: TransactionData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(12.dp).height(2.dp).clip(RoundedCornerShape(1.dp)).background(ExpenseRed))
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Normal, maxLines = 1)
            Text(text = transaction.details, color = TextGray, fontSize = 12.sp, maxLines = 1)
        }

        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${transaction.amount} ${transaction.currency}", color = ExpenseRed, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add",
                tint = PrimaryBlue,
                modifier = Modifier.size(24.dp).border(1.dp, PrimaryBlue, CircleShape).padding(2.dp)
            )
        }
    }
}

data class TransactionData(val title: String, val details: String, val amount: String, val currency: String = "EUR")
data class DateGroupData(val date: String, val transactions: List<TransactionData>)

private val mockTransactionGroups = listOf(
    DateGroupData("17 April 2026", listOf(TransactionData("KAUFLAND", "GP NÁKUP POS", "2.57"))),
    DateGroupData("16 April 2026", listOf(
        TransactionData("KAUFLAND", "GP NÁKUP POS", "1.85"),
        TransactionData("Saint Coffee Kosice", "GP NÁKUP POS", "2.50"),
        TransactionData("DO PIZZE", "GP NÁKUP POS", "1.50"),
        TransactionData("Ubian.sk", "GP NÁKUP POS", "15.00")
    ))
)

// --- Bottom Sheet & Member Components ---
@Composable
fun DebtorsBottomSheetContent(
    availableMembers: List<GroupMember>,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredMembers = availableMembers.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f).padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text("Select participants", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name", color = TextGray) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = DarkCardBorder, focusedBorderColor = PrimaryBlue,
                unfocusedContainerColor = InputBackground, focusedContainerColor = InputBackground,
                unfocusedTextColor = Color.White, focusedTextColor = Color.White,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().clickable {
                val newSelection = if (selectedIds.size == availableMembers.size) emptySet() else availableMembers.map { it.id }.toSet()
                onSelectionChange(newSelection)
            }.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Select All", color = PrimaryBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Icon(
                imageVector = if (selectedIds.size == availableMembers.size) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                contentDescription = null, tint = PrimaryBlue
            )
        }

        LazyColumn {
            items(filteredMembers) { member ->
                val isSelected = selectedIds.contains(member.id)
                SelectableMemberItem(
                    member = member, isSelected = isSelected,
                    onClick = {
                        val newSet = if (isSelected) selectedIds - member.id else selectedIds + member.id
                        onSelectionChange(newSet)
                    }
                )
            }
        }
    }
}

@Composable
private fun DebtorItemStyle(member: GroupMember) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(member.avatarColor), contentAlignment = Alignment.Center) {
            Text(member.initials, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(member.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(member.detail, color = TextGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SelectableMemberItem(member: GroupMember, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(member.avatarColor), contentAlignment = Alignment.Center) {
            Text(member.initials, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(member.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(member.detail, color = TextGray, fontSize = 12.sp)
        }
        Icon(
            imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = null, tint = if (isSelected) PrimaryBlue else TextGray, modifier = Modifier.size(24.dp)
        )
    }
}