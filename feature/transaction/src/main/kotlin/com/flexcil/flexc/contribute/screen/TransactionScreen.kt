package com.flexcil.flexc.contribute.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransactionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Header ---
        TransactionHeader()

        // --- Search Bar ---
        SearchBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // --- Account Selector ---
        AccountSelector(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Товстий розділювач між шапкою і списком
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFF0A0A0C)) // Дуже темний спейсер
        )

        // --- Transactions List ---
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            mockTransactionGroups.forEach { group ->
                // Date Header
                item {
                    Text(
                        text = group.date,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }

                // Transactions for this date
                items(group.transactions) { transaction ->
                    TransactionItem(transaction = transaction)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Account transactions",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Custom Search Icon with Settings Badge
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable { /* Open advanced search/settings */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            // Small settings gear badge in top right corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background), // Background to cut out the search icon behind it
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier) {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = {
            Text(
                text = "Search",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}

@Composable
private fun AccountSelector(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* Change account */ },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "For account",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 2.dp)
        )

        Row(verticalAlignment = Alignment.Top) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Klymiuk Vladyslav",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                // IBAN with partial bold
                Text(
                    text = buildAnnotatedString {
                        append("SK93 ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("1100 ")
                        }
                        append("0000 00")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("29 3858 0850")
                        }
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = "Select Account",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red Minus Indicator
        Box(
            modifier = Modifier
                .width(12.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(MaterialTheme.colorScheme.error)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Transaction Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (transaction.subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.subtitle,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.details,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Amount & CO2
        Column(horizontalAlignment = Alignment.End) {
            // Amount
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = transaction.amount,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = transaction.currency,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // CO2 Info
            if (transaction.co2 != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Cloud,
                        contentDescription = "CO2",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(transaction.co2)
                            append(" kg CO")
                            withStyle(
                                style = SpanStyle(
                                    baselineShift = BaselineShift.Subscript,
                                    fontSize = 9.sp
                                )
                            ) {
                                append("2")
                            }
                            append("e")
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// --- Data Models & Mock Data ---

data class Transaction(
    val title: String,
    val subtitle: String?,
    val details: String,
    val amount: String,
    val currency: String = "EUR",
    val co2: String?
)

data class DateGroup(
    val date: String,
    val transactions: List<Transaction>
)

private val mockTransactionGroups = listOf(
    DateGroup(
        date = "17 April 2026",
        transactions = listOf(
            Transaction("KAUFLAND", "2420,KE,TORYSKA", "GP NÁKUP POS", "2,57", co2 = "3,04")
        )
    ),
    DateGroup(
        date = "16 April 2026",
        transactions = listOf(
            Transaction("KAUFLAND", "2420,KE,TORYSKA", "GP NÁKUP POS", "1,85", co2 = "2,19"),
            Transaction("Saint Coffee Kosice", null, "GP NÁKUP POS", "2,50", co2 = "2,96"),
            Transaction("DO PIZZE", null, "GP NÁKUP POS", "1,50", co2 = "1,77"),
            Transaction("Ubian.sk", null, "GP NÁKUP POS", "15,00", co2 = null)
        )
    )
)
