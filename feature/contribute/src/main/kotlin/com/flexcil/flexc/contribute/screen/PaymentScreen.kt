package com.flexcil.flexc.contribute.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.LocalNavigator

// --- Specific Colors from Design ---
private val AppBackground = Color(0xFF14151A)
private val InputBackground = Color(0xFF1C1D22)
private val PrimaryBlue = Color(0xFF2B88F0)
private val TextGray = Color(0xFF8B8D98)
private val DividerGray = Color(0xFF2F3036)
private val ThickDividerBlack = Color(0xFF000000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(screen: AppScreen.PaymentScreen = AppScreen.PaymentScreen()) {
    var beneficiaryName by remember { mutableStateOf(screen.beneficiaryName ?: "") }
    var iban by remember { mutableStateOf(screen.iban ?: "") }
    var amount by remember { mutableStateOf(screen.amount ?: "0,00") }
    var information by remember { mutableStateOf(screen.information ?: "") }
    val payerName by remember { mutableStateOf(screen.payerName ?: "Klymiuk Vladyslav") }
    val payerIban by remember { mutableStateOf(screen.payerIban ?: "SK93 1100 0000 0029 3858 0850") }
    val payerBalance by remember { mutableStateOf(screen.payerBalance ?: "2,47 EUR") }

    var showTransactionPicker by remember { mutableStateOf(false) }

    if (showTransactionPicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showTransactionPicker = false },
            sheetState = sheetState,
            containerColor = AppBackground
        ) {
            TransactionPickerContent(
                onTransactionSelected = { transaction ->
                    beneficiaryName = transaction.title
                    amount = transaction.amount
                    information = transaction.details
                    showTransactionPicker = false
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // --- Header ---
        PaymentHeader()

        // --- Scrollable Content ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            PayerSection(payerName = payerName, payerIban = payerIban, balance = payerBalance)
            ThickDivider()

            BeneficiarySection(
                beneficiaryName = beneficiaryName,
                onBeneficiaryNameChange = { beneficiaryName = it },
                iban = iban,
                onIbanChange = { iban = it },
                amount = amount,
                onAmountChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*([.,])?\\d{0,2}\$"))) {
                        amount = newValue
                    }
                },
                information = information,
                onInformationChange = { information = it },
                onFromTransactionsClick = { showTransactionPicker = true }
            )
            ThickDivider()

            OtherDataSection()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Sticky Bottom Button ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppBackground)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            val navigator = LocalNavigator.current
            Button(
                onClick = { 
                    navigator.launchScreen(AppScreen.TransactionScreen)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Review",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PaymentHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = PrimaryBlue,
            modifier = Modifier
                .size(28.dp)
                .clickable { /* Handle back */ }
        )

        Text(
            text = "Payment",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        Icon(
            imageVector = Icons.Outlined.HelpOutline,
            contentDescription = "Help",
            tint = PrimaryBlue,
            modifier = Modifier
                .size(28.dp)
                .clickable { /* Handle help */ }
        )
    }
}

@Composable
private fun PayerSection(payerName: String, payerIban: String, balance: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Payer Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Payer", color = TextGray, fontSize = 14.sp)
            Text(text = payerName, color = Color.White, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // IBAN Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "IBAN", color = TextGray, fontSize = 14.sp)
            Text(
                text = payerIban,
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Account Balance Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Account balance", color = TextGray, fontSize = 14.sp)
            Text(text = balance, color = Color.White, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(color = DividerGray)
        Spacer(modifier = Modifier.height(16.dp))

        // Change Payer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle change payer */ },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Change payer",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BeneficiarySection(
    beneficiaryName: String,
    onBeneficiaryNameChange: (String) -> Unit,
    iban: String,
    onIbanChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    information: String,
    onInformationChange: (String) -> Unit,
    onFromTransactionsClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Beneficiary",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                Text(
                    text = "From transactions",
                    color = PrimaryBlue,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onFromTransactionsClick() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Scan IBAN/QR code",
                    color = PrimaryBlue,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* Handle scan */ }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        PaymentInputField(
            label = "Choose beneficiary",
            value = beneficiaryName,
            onValueChange = onBeneficiaryNameChange,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(1.dp, PrimaryBlue, CircleShape)
                        .padding(2.dp)
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(
            label = "IBAN/Foreign account number",
            isRequired = true,
            value = iban,
            onValueChange = onIbanChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(
            label = "Beneficiary name",
            value = beneficiaryName,
            onValueChange = onBeneficiaryNameChange
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Amount and Currency Row
        Row(modifier = Modifier.fillMaxWidth()) {
            PaymentInputField(
                label = "Amount",
                isRequired = true,
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Currency Dropdown Box
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(InputBackground)
                    .clickable { /* Open currency selection */ }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "EUR", color = Color.White, fontSize = 16.sp)
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        tint = PrimaryBlue
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Maturity Date
        PaymentInputField(
            label = "Maturity date",
            isRequired = true,
            value = "ASAP",
            onValueChange = { /* Usually selected via picker */ },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(
            label = "Information for beneficiary",
            value = information,
            onValueChange = onInformationChange,
            minHeight = 100.dp,
            alignTop = true
        )
    }
}

@Composable
private fun OtherDataSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        Text(
            text = "Other data",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        PaymentInputField(label = "Variable symbol")
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(label = "Specific symbol")
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(
            label = "Constant symbol",
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(
            label = "Payer's reference",
            minHeight = 100.dp,
            alignTop = true
        )
    }
}

// --- Transaction Picker Components ---

@Composable
private fun TransactionPickerContent(onTransactionSelected: (TransactionData) -> Unit) {
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
                    HorizontalDivider(
                        color = DividerGray.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionPickerItem(transaction: TransactionData, onClick: () -> Unit) {
    val ExpenseRed = Color(0xFFE55D5D)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red Minus Indicator
        Box(
            modifier = Modifier
                .width(12.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(ExpenseRed)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Transaction Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (transaction.subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.subtitle,
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.details,
                color = TextGray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Amount & Add Icon
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${transaction.amount} ${transaction.currency}",
                    color = ExpenseRed,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = PrimaryBlue,
                    modifier = Modifier
                        .size(24.dp)
                        .border(1.dp, PrimaryBlue, CircleShape)
                        .padding(2.dp)
                )
            }

            if (transaction.co2 != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Cloud,
                        contentDescription = "CO2",
                        tint = TextGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(transaction.co2)
                            append(" kg CO")
                            withStyle(style = SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = 9.sp)) {
                                append("2")
                            }
                            append("e")
                        },
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

private data class TransactionData(
    val title: String,
    val subtitle: String? = null,
    val details: String,
    val amount: String,
    val currency: String = "EUR",
    val co2: String? = null
)

private data class DateGroupData(
    val date: String,
    val transactions: List<TransactionData>
)

private val mockTransactionGroups = listOf(
    DateGroupData(
        date = "17 April 2026",
        transactions = listOf(
            TransactionData("KAUFLAND", "2420,KE,TORYSKA", "GP NÁKUP POS", "2,57", co2 = "3,04")
        )
    ),
    DateGroupData(
        date = "16 April 2026",
        transactions = listOf(
            TransactionData("KAUFLAND", "2420,KE,TORYSKA", "GP NÁKUP POS", "1,85", co2 = "2,19"),
            TransactionData("Saint Coffee Kosice", null, "GP NÁKUP POS", "2,50", co2 = "2,96"),
            TransactionData("DO PIZZE", null, "GP NÁKUP POS", "1,50", co2 = "1,77"),
            TransactionData("Ubian.sk", null, "GP NÁKUP POS", "15,00", co2 = null)
        )
    )
)

// --- Custom Reusable Components ---

@Composable
private fun ThickDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(ThickDividerBlack)
    )
}

@Composable
private fun PaymentInputField(
    label: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    minHeight: androidx.compose.ui.unit.Dp = 56.dp,
    alignTop: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(InputBackground)
            .padding(horizontal = 16.dp, vertical = if (alignTop) 16.dp else 0.dp),
        contentAlignment = if (alignTop) Alignment.TopStart else Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = if (alignTop) Alignment.Top else Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Label with optional blue asterisk
            Text(
                text = buildAnnotatedString {
                    append(label)
                    if (isRequired) {
                        withStyle(style = SpanStyle(color = PrimaryBlue)) {
                            append(" *")
                        }
                    }
                },
                color = TextGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = if (alignTop) 2.dp else 0.dp)
            )

            // Right side Content (TextField and/or Icon)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    ),
                    cursorBrush = SolidColor(PrimaryBlue),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterEnd) {
                            innerTextField()
                        }
                    }
                )

                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    trailingIcon()
                }
            }
        }
    }
}