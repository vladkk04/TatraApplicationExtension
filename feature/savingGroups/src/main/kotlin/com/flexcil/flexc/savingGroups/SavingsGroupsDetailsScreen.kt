package com.flexcil.flexc.savingGroups

import android.R.attr.padding
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.flexcil.flexc.core.model.ExpenseRequest
import com.flexcil.flexc.core.model.GlobalMockData
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.LocalNavigator

private val ChartGreen = Color(0xFF66E289)
private val AvatarRed = Color(0xFFE55D5D)
private val AvatarOlive = Color(0xFFD2BCA0)
private val AvatarPurple = Color(0xFF2B88F0)
private val AvatarRose = Color(0xFFFBCFE8)

data class Contributor(
    val id: String,
    val name: String,
    val initials: String,
    val amount: Double,
    val color: Color
)

@Composable
fun SavingsGroupsDetailsScreen(groupName: String? = null) {
    val navigator = LocalNavigator.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var showRequestDialog by remember { mutableStateOf(false) }
    
    val isMockGroup = groupName == "Saving for Party"
    val requests = if (isMockGroup) GlobalMockData.pendingRequests else remember { mutableStateListOf() }
    
    val incomeContributors = if (isMockGroup) {
        listOf(
            Contributor("1", "Vladyslav Dorosh", "VD", 1700.0, AvatarRed),
            Contributor("2", "Vladyslav Klymiuk", "VK", 900.0, AvatarOlive),
            Contributor("3", "Daniil Dryzhov", "DD", 1100.0, AvatarPurple),
            Contributor("4", "Danyil Yatluk", "DY", 700.0, AvatarRose)
        )
    } else emptyList()

    val expenseContributors = if (isMockGroup) {
        listOf(
            Contributor("1", "Vladyslav Dorosh", "VD", -500.0, AvatarRed),
            Contributor("2", "Vladyslav Klymiuk", "VK", -1200.0, AvatarOlive),
            Contributor("3", "Daniil Dryzhov", "DD", -300.0, AvatarPurple),
            Contributor("4", "Danyil Yatluk", "DY", -400.0, AvatarRose)
        )
    } else emptyList()

    val currentContributors = if (selectedTab == 0) incomeContributors else expenseContributors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SavingsOverviewCard(currentContributors, isExpense = selectedTab == 1)
        Spacer(modifier = Modifier.height(24.dp))
        SavingsTabs(selectedTab) { selectedTab = it }
        Spacer(modifier = Modifier.height(24.dp))
        RequestsSection(
            requests = requests,
            onApprove = { id ->
                val index = requests.indexOfFirst { it.id == id }
                if (index != -1) {
                    val request = requests[index]
                    requests[index] = request.copy(
                        approvedCount = request.approvedCount + 1,
                        isSelfApproved = true
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        val vladIban = "SK93 1100 0000 0029 3858 0850"
        val partyIban = "SK11 1100 0000 0012 3456 7890"

        ContributionsSection(
            contributors = currentContributors,
            isExpense = selectedTab == 1,
            onRequestExpense = {
                navigator.launchScreen(
                    AppScreen.RequestExpenseScreen(
                        beneficiaryName = "Vladyslav Klymiuk",
                        iban = vladIban,
                        amount = "150,00",
                        information = "Request for Pizza",
                        payerName = "Party",
                        payerIban = partyIban,
                        payerBalance = "4 400,00 EUR"
                    )
                )
            },
            onContribute = {
                navigator.launchScreen(
                    AppScreen.PaymentScreen(
                        beneficiaryName = "Party",
                        iban = partyIban,
                        amount = "",
                        information = "Contribution to Savings Group",
                        payerName = "Vladyslav Klymiuk",
                        payerIban = vladIban,
                        payerBalance = "2,47 EUR"
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(100.dp))
    }

    if (showRequestDialog) {
        AddRequestDialog(
            onDismiss = { showRequestDialog = false },
            onSave = { amount ->
                requests.add(
                    ExpenseRequest(
                        id = java.util.UUID.randomUUID().toString(),
                        requesterName = "Vladyslav Dorosh", // Mock user
                        requesterInitials = "VD",
                        amount = amount
                    )
                )
                showRequestDialog = false
            }
        )
    }
}

@Composable
private fun SavingsOverviewCard(contributors: List<Contributor>, isExpense: Boolean) {
    val totalAmount = contributors.sumOf { it.amount }
    var selectedContributor by remember { mutableStateOf<Contributor?>(null) }
    // Reset selection when tab changes
    LaunchedEffect(isExpense) { selectedContributor = null }

    val displayColor = if (isExpense) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    if (isExpense) "Total Expenses" else "Saving for Party",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SK11 1100 0000 0012 3456 7890",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        "Group balance",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "${kotlin.math.abs(totalAmount).toInt()} EUR",
                        color = if (isExpense) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                SavingsChart(modifier = Modifier.size(110.dp, 70.dp), isExpense = isExpense)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Interactive Segmented Bar
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        val totalMagnitude = contributors.sumOf { kotlin.math.abs(it.amount) }
                        contributors.forEach { contributor ->
                            val weight = (kotlin.math.abs(contributor.amount) / totalMagnitude).toFloat()
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(weight)
                                    .background(contributor.color)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        selectedContributor = if (selectedContributor == contributor) null else contributor
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavingsChart(modifier: Modifier = Modifier, isExpense: Boolean = false) {
    val chartColor = if (isExpense) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        val path = Path().apply {
            if (isExpense) {
                // Downward trend for expenses
                moveTo(0f, height * 0.2f)
                cubicTo(width * 0.2f, height * 0.1f, width * 0.4f, height * 0.8f, width * 0.6f, height * 0.5f)
                cubicTo(width * 0.8f, height * 0.2f, width * 0.9f, height * 0.9f, width, height * 0.8f)
            } else {
                // Upward trend for income
                moveTo(0f, height * 0.8f)
                cubicTo(width * 0.2f, height * 0.9f, width * 0.4f, height * 0.2f, width * 0.6f, height * 0.5f)
                cubicTo(width * 0.8f, height * 0.8f, width * 0.9f, height * 0.1f, width, height * 0.2f)
            }
        }
        
        drawPath(
            path = path,
            color = chartColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(chartColor.copy(alpha = 0.2f), Color.Transparent),
                startY = 0f,
                endY = height
            ),
            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun SavingsTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Incomes", "Expenses").forEachIndexed { index, text ->
                val selected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (selected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ContributionsSection(
    contributors: List<Contributor>,
    isExpense: Boolean,
    onRequestExpense: () -> Unit,
    onContribute: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (isExpense) "Expenses" else "Contributions",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (isExpense) "Request Expense" else "Contribute",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { 
                    if (isExpense) onRequestExpense() else onContribute()
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        contributors.forEach { contributor ->
            ContributorItem(contributor, isExpense)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ContributorItem(contributor: Contributor, isExpense: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(contributor.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contributor.initials,
                    color = contributor.color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    contributor.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "SK30 **** **** 3664",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            Text(
                "${kotlin.math.abs(contributor.amount).toInt()} EUR",
                color = if (isExpense) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RequestsSection(
    requests: List<ExpenseRequest>,
    onApprove: (String) -> Unit
) {
    if (requests.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Pending Approval",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        requests.forEach { request ->
            RequestItem(request, onApprove)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun RequestItem(
    request: ExpenseRequest,
    onApprove: (String) -> Unit
) {
    val isApproved = request.isSelfApproved

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isApproved) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f) else AvatarOlive.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    request.requesterInitials,
                    color = if (isApproved) MaterialTheme.colorScheme.onSurfaceVariant else AvatarOlive.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    request.requesterName,
                    color = if (isApproved) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${request.amount.toInt()} EUR",
                    color = if (isApproved) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            if (isApproved) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                ) {
                    Text(
                        "Approved ${request.approvedCount}/${request.totalNeeded}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            } else {
                Button(
                    onClick = { onApprove(request.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Approve", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun AddRequestDialog(
    onDismiss: () -> Unit,
    onSave: (amount: Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            color = Color(0xFF1C1C1E),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Request Expense",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) amount = it },
                    label = { Text("Amount (EUR)", color = Color.White.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                    )
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                    }
                    Button(
                        onClick = {
                            val amountDouble = amount.toDoubleOrNull()
                            if (amountDouble != null) {
                                onSave(amountDouble)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Request", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
