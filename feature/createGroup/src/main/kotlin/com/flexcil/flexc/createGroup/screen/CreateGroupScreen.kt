package com.flexcil.flexc.createGroup.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Contacts
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

// --- Custom Colors ---
private val AvatarRed = Color(0xFF6B1E2C)
private val AvatarOlive = Color(0xFF4C5D23)
private val AvatarPurple = Color(0xFF2E235D)
private val AvatarRose = Color(0xFFC08985)
private val DarkCardBorder = Color(0xFF2F3036)
private val TatraBlue = Color(0xFF3B82F6) // Accent blue

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

// Enum for navigating inside the Bottom Sheet
enum class BeneficiarySheetScreen {
    LIST, ADD_NEW
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen() {
    var selectedGroupType by remember { mutableStateOf(GroupType.DEBT_GROUP) }
    var groupName by remember { mutableStateOf("") }

    var currencyExpanded by remember { mutableStateOf(false) }
    val currencies = listOf("EUR", "CZK", "USD", "GBP", "PLN", "HUF")
    var selectedCurrency by remember { mutableStateOf(currencies[0]) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedMemberIds by remember { mutableStateOf(setOf<String>()) }

    // Hoisted Mutable List so we can add new members dynamically
    val availableMembers = remember {
        mutableStateListOf(
            Member("1", "Vladyslav Dorosh", "VD", "+421 900 123 456", AvatarRed),
            Member("2", "Vladyslav Klymiuk", "VK", "+421 911 654 321", AvatarOlive),
            Member("3", "Daniil Dryzhov", "DD", "+421 944 111 222", AvatarPurple),
            Member("4", "Danyil Yatluk", "DY", "+421 950 999 888", AvatarRose)
        )
    }

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            BeneficiaryBottomSheetManager(
                availableMembers = availableMembers,
                selectedIds = selectedMemberIds,
                onSelectionChange = { selectedMemberIds = it },
                onAddMember = { newMember ->
                    availableMembers.add(newMember)
                    selectedMemberIds = selectedMemberIds + newMember.id // Auto-select the newly added member
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
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

            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            CustomInputField(
                label = "Group Name",
                value = groupName,
                onValueChange = { groupName = it },
                placeholder = "Enter group name..."
            )

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = !currencyExpanded }
            ) {
                CustomInputField(
                    label = "Currency",
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .background(Color(0xFF141416))
                    .clickable { showBottomSheet = true }
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedMemberIds.isEmpty()) "Choose members" else "${selectedMemberIds.size} members selected",
                    color = if (selectedMemberIds.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
                Icon(
                    imageVector = Icons.Rounded.Contacts,
                    contentDescription = "Choose Members",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

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
            Text("Create Group", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// --- Bottom Sheet Navigation Manager ---
@Composable
fun BeneficiaryBottomSheetManager(
    availableMembers: List<Member>,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    onAddMember: (Member) -> Unit
) {
    var currentScreen by remember { mutableStateOf(BeneficiarySheetScreen.LIST) }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState == BeneficiarySheetScreen.ADD_NEW) {
                (slideInHorizontally(animationSpec = tween(300)) { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally(animationSpec = tween(300)) { width -> -width } + fadeOut())
            } else {
                (slideInHorizontally(animationSpec = tween(300)) { width -> -width } + fadeIn()).togetherWith(
                    slideOutHorizontally(animationSpec = tween(300)) { width -> width } + fadeOut())
            }
        },
        label = "BottomSheetNavigation"
    ) { screen ->
        when (screen) {
            BeneficiarySheetScreen.LIST -> {
                BeneficiaryListScreen(
                    availableMembers = availableMembers,
                    selectedIds = selectedIds,
                    onSelectionChange = onSelectionChange,
                    onNavigateToAdd = { currentScreen = BeneficiarySheetScreen.ADD_NEW }
                )
            }
            BeneficiarySheetScreen.ADD_NEW -> {
                AddNewBeneficiaryScreen(
                    onBack = { currentScreen = BeneficiarySheetScreen.LIST },
                    onSave = { newMember ->
                        onAddMember(newMember)
                        currentScreen = BeneficiarySheetScreen.LIST
                    }
                )
            }
        }
    }
}

// --- Screen 1: The List ---
@Composable
fun BeneficiaryListScreen(
    availableMembers: List<Member>,
    selectedIds: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredMembers = availableMembers.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.detail.contains(searchQuery, ignoreCase = true)
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
                unfocusedContainerColor = Color(0xFF141416),
                focusedContainerColor = Color(0xFF141416)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add beneficiary",
            color = TatraBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .clickable { onNavigateToAdd() }
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

// --- Screen 2: Add New Beneficiary Form ---
@Composable
fun AddNewBeneficiaryScreen(
    onBack: () -> Unit,
    onSave: (Member) -> Unit
) {
    var customName by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }
    var beneficiaryName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = "Back", tint = TatraBlue)
            }
            Text(
                text = "New beneficiary",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Help action */ }) {
                Icon(Icons.Rounded.HelpOutline, contentDescription = "Help", tint = TatraBlue)
            }
        }

        HorizontalDivider(color = DarkCardBorder, thickness = 1.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Sub-header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Beneficiary details",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Scan IBAN",
                    color = TatraBlue,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* Scan action */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Inputs
            TatraFormInputField("Custom beneficiary name", customName) { customName = it }
            Spacer(modifier = Modifier.height(16.dp))
            TatraFormInputField("IBAN/Foreign account number", iban) { iban = it }
            Spacer(modifier = Modifier.height(16.dp))
            TatraFormInputField("Beneficiary name", beneficiaryName) { beneficiaryName = it }

            Spacer(modifier = Modifier.height(32.dp))

            // More Information Expandable Text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Expand action */ },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "More information", color = TatraBlue, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = null, tint = TatraBlue, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // Add Button
            Button(
                onClick = {
                    if (beneficiaryName.isNotBlank() && iban.isNotBlank()) {
                        // Generate fake ID and Initials
                        val id = UUID.randomUUID().toString()
                        val initials = beneficiaryName.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")

                        val newMember = Member(
                            id = id,
                            name = if(customName.isNotBlank()) customName else beneficiaryName,
                            initials = initials.ifBlank { "?" },
                            detail = iban,
                            avatarColor = listOf(AvatarRed, AvatarOlive, AvatarPurple, AvatarRose).random()
                        )
                        onSave(newMember)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TatraBlue, contentColor = Color.White)
            ) {
                Text("Add beneficiary", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Custom Input style to exactly match the screenshot's solid dark boxes
@Composable
fun TatraFormInputField(hint: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = buildAnnotatedString {
                    append(hint)
                    append(" ")
                    withStyle(style = SpanStyle(color = TatraBlue)) { append("*") }
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF141416), // Dark solid background
            unfocusedContainerColor = Color(0xFF141416),
            focusedIndicatorColor = Color.Transparent, // No bottom line
            unfocusedIndicatorColor = Color.Transparent, // No bottom line
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        singleLine = true
    )
}

// --- Reusable Shared Components Below ---

@Composable
fun SegmentedControl(options: List<String>, selectedIndex: Int, onOptionSelected: (Int) -> Unit) {
    val containerColor = Color(0xFF141416)
    val selectedColor = Color(0xFF2F3038)
    val textColorSelected = Color(0xFFFFFFFF)
    val textColorUnselected = Color(0xFF8B8B90)

    Row(
        modifier = Modifier.fillMaxWidth().height(58.dp).background(containerColor, RoundedCornerShape(16.dp)).padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, text ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) selectedColor else Color.Transparent)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onOptionSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(text, color = if (isSelected) textColorSelected else textColorUnselected, fontSize = 15.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun CustomInputField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier, placeholder: String = "", readOnly: Boolean = false, trailingIcon: @Composable (() -> Unit)? = null) {
    Column(modifier = modifier) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value, onValueChange = onValueChange, readOnly = readOnly,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), fontSize = 16.sp) },
            trailingIcon = trailingIcon, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline, focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface, focusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true
        )
    }
}

@Composable
private fun SelectableMemberItem(member: Member, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else DarkCardBorder
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp)).background(Color(0xFF141416))
            .clickable(onClick = onClick).padding(12.dp),
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