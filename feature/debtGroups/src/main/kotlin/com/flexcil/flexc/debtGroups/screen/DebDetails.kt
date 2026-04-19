package com.flexcil.flexc.debtGroups.screen

import android.R.attr.onClick
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
import com.flexcil.flexc.debtGroups.DebDetailsViewModel

@Composable
fun DepDetailsScreen() {
    val viewModel = hiltViewModel<DebDetailsViewModel>()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        ExpenseGroupList(viewModel::navigateToPayment)
    }
}

@Composable
private fun ExpenseGroupList(
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            ExpandableExpenseGroup(
                title = "The bill for yesterday’s party at the restaurant",
                subtitle = "Paid",
                avatars = 3,
                checkedAvatarIndex = 1,
                isFirstAvatarEmpty = false,
                amountValue = "120.00",
                currency = "EUR",
                buttonState = ButtonState.PAID_CHECKED
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            ExpandableExpenseGroup(
                title = "BOLT",
                subtitle = "Created by Danyil Yatluk",
                avatars = 3,
                checkedAvatarIndex = null,
                isFirstAvatarEmpty = true,
                amountValue = "30.00",
                currency = "EUR",
                buttonState = ButtonState.PAY_NOW,
                onClick = onClick
            )
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
    buttonState: ButtonState,
    onClick: () -> Unit = {}
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
            buttonState = buttonState,
            onClick = onClick
        )

        AnimatedVisibility(visible = isExpanded) {
            DebtorsStatusSection()
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
    buttonState: ButtonState,
    onClick: () -> Unit
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

            ActionButton(
                state = buttonState,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun DebtorsStatusSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp)
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        DebtorStatusItem("Danyil Yatluk", hasPaid = true)
        Spacer(modifier = Modifier.height(8.dp))
        DebtorStatusItem("Vladyslav Dorosh", hasPaid = false)
        Spacer(modifier = Modifier.height(8.dp))
        DebtorStatusItem("Sarah Miller", hasPaid = true)
    }
}

@Composable
private fun DebtorStatusItem(name: String, hasPaid: Boolean) {
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

        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

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
    PAID_CHECKED, PAY_NOW
}

@Composable
private fun ActionButton(
    state: ButtonState,
    onClick: () -> Unit = {}
) {
    val containerColor = when (state) {
        ButtonState.PAY_NOW -> Color(0xFF007AFF)
        ButtonState.PAID_CHECKED -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (state) {
        ButtonState.PAY_NOW -> Color.White
        ButtonState.PAID_CHECKED -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .height(34.dp)
            .width(88.dp),
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
