package com.flexcil.flexc.debtGroups.screen

import java.util.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.util.Locale
import com.flexcil.flexc.debtGroups.DebDetailsViewModel

@Composable
fun DepDetailsScreen(groupName: String? = null) {

    val viewModel = hiltViewModel<DebDetailsViewModel>()
    val expenses by viewModel.expenses.collectAsState()

    val isMockGroup = groupName == "Food Sharing" || groupName == "Party Debts"
    val filteredExpenses = if (isMockGroup) {
        if (groupName == "Food Sharing") {
            listOf(
                com.flexcil.flexc.core.model.Expense(
                    title = "Dinner at Sky City",
                    subtitle = "Oleksandr",
                    avatars = 3,
                    checkedAvatarIndex = null,
                    isFirstAvatarEmpty = true,
                    amountValue = "90.00",
                    currency = "EUR",
                    buttonState = "PAY_NOW",
                    isCreatedByMe = false
                )
            )
        } else {
            // Party Debts or others that have existing mock data
            expenses
        }
    } else {
        emptyList()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = groupName ?: "Expenses",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Add",
                color = Color(0xFF007AFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable {
                        if (groupName == "Food Sharing") {
                            // Show a simple snackbar or log for now, 
                            // as we don't have a real persistent store for "Food Sharing" yet
                        } else {
                            viewModel.navigateToCreateSpendingScreen()
                        }
                    }
                    .padding(4.dp)
            )
        }

        ExpenseGroupList(filteredExpenses)
    }
}

@Composable
private fun ExpenseGroupList(expenses: List<com.flexcil.flexc.core.model.Expense>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            expenses.forEachIndexed { index, expense ->
                ExpandableExpenseGroup(
                    title = expense.title,
                    subtitle = expense.subtitle,
                    avatars = expense.avatars,
                    checkedAvatarIndex = expense.checkedAvatarIndex,
                    isFirstAvatarEmpty = expense.isFirstAvatarEmpty,
                    amountValue = expense.amountValue,
                    currency = expense.currency,
                    buttonState = when {
                        expense.isCreatedByMe -> ButtonState.YOURS
                        expense.buttonState == "PAID_CHECKED" -> ButtonState.PAID_CHECKED
                        else -> ButtonState.PAY_NOW
                    }
                )

                if (index < expenses.size - 1) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandableExpenseGroup(
    title: String,
    subtitle: String?,
    avatars: Int,
    checkedAvatarIndex: Int? = null,
    isFirstAvatarEmpty: Boolean = false,
    amountValue: String,
    currency: String,
    buttonState: ButtonState
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        ExpenseSplitRow(
            title = title,
            subtitle = subtitle,
            avatars = avatars,
            checkedAvatarIndex = checkedAvatarIndex,
            isFirstAvatarEmpty = isFirstAvatarEmpty,
            amountValue = amountValue,
            currency = currency,
            buttonState = buttonState
        )

        AnimatedVisibility(visible = isExpanded) {
            val amountPerPersonValue = try {
                val total = amountValue.replace(",", ".").toDouble()
                String.format(Locale.US, "%.2f %s", total / avatars, currency)
            } catch (e: Exception) {
                "0.00 $currency"
            }
            DebtorsStatusSection(
                amountPerPerson = amountPerPersonValue,
                creatorName = subtitle ?: "",
                isMePaid = buttonState == ButtonState.PAID_CHECKED || buttonState == ButtonState.YOURS
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
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CustomOverlappingAvatars(
                count = avatars,
                checkedIndex = checkedAvatarIndex,
                firstEmpty = isFirstAvatarEmpty
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = amountValue,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currency,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ActionButton(state = buttonState)
        }
    }
}

@Composable
private fun DebtorsStatusSection(amountPerPerson: String, creatorName: String, isMePaid: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp)
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        DebtorStatusItem("Danyil Yatluk", hasPaid = isMePaid, amountPerPerson = amountPerPerson)
        Spacer(modifier = Modifier.height(8.dp))
        DebtorStatusItem("Vladyslav Dorosh", hasPaid = creatorName == "Vladyslav Dorosh", amountPerPerson = amountPerPerson)
        Spacer(modifier = Modifier.height(8.dp))
        DebtorStatusItem("Daniil Dryzhov", hasPaid = creatorName == "Daniil Dryzhov", amountPerPerson = amountPerPerson)
    }
}

@Composable
private fun DebtorStatusItem(name: String, hasPaid: Boolean, amountPerPerson: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF252528))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = amountPerPerson,
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasPaid) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Paid",
                    tint = Color(0xFF00D67D),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Paid",
                    color = Color(0xFF00D67D),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = "Pending",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Pending",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CustomOverlappingAvatars(
    count: Int,
    checkedIndex: Int? = null,
    firstEmpty: Boolean = false
) {
    val avatarColors = listOf(Color(0xFF8B8D98), Color(0xFFD2BCA0), Color(0xFF2B88F0))

    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier.offset(x = if (i > 0) (-8 * i).dp else 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (firstEmpty && i == 0) Color(0xFFE0E0E0) else avatarColors[i % avatarColors.size])
                        .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (!(firstEmpty && i == 0)) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (checkedIndex == i) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00D67D))
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
    PAID_CHECKED, PAY_NOW, YOURS
}

@Composable
private fun ActionButton(state: ButtonState) {

    val viewModel = hiltViewModel<DebDetailsViewModel>()

    val containerColor = when (state) {
        ButtonState.PAY_NOW -> Color(0xFF007AFF)
        ButtonState.PAID_CHECKED, ButtonState.YOURS -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (state) {
        ButtonState.PAY_NOW -> Color.White
        ButtonState.PAID_CHECKED, ButtonState.YOURS -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Button(
        onClick = { if (state == ButtonState.PAY_NOW) viewModel.navigateToPayment() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .height(34.dp)
            .width(88.dp),
        contentPadding = PaddingValues(0.dp),
        enabled = state == ButtonState.PAY_NOW
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (state) {
                    ButtonState.PAY_NOW -> "Pay Now"
                    ButtonState.PAID_CHECKED -> "Paid"
                    ButtonState.YOURS -> "Yours"
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            if (state == ButtonState.PAID_CHECKED) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
