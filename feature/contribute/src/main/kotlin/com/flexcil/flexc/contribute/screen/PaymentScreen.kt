package com.flexcil.flexc.contribute.screen

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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

// --- Specific Colors from Design ---
private val AppBackground = Color(0xFF14151A)
private val InputBackground = Color(0xFF1C1D22)
private val PrimaryBlue = Color(0xFF2B88F0)
private val TextGray = Color(0xFF8B8D98)
private val DividerGray = Color(0xFF2F3036)
private val ThickDividerBlack = Color(0xFF000000)

@Composable
fun PaymentScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // --- Scrollable Content ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            PayerSection()
            ThickDivider()

            BeneficiarySection()
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
            Button(
                onClick = { /* Handle Review */ },
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
private fun PayerSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Payer Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Payer", color = TextGray, fontSize = 14.sp)
            Text(text = "Klymiuk Vladyslav", color = Color.White, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // IBAN Row with partial bold text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "IBAN", color = TextGray, fontSize = 14.sp)
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
            Text(text = "2,47 EUR", color = Color.White, fontSize = 14.sp)
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
private fun BeneficiarySection() {
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
            Text(
                text = "Scan IBAN/QR code",
                color = PrimaryBlue,
                fontSize = 16.sp,
                modifier = Modifier.clickable { /* Handle scan */ }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        PaymentInputField(
            label = "Choose beneficiary",
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
            isRequired = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        PaymentInputField(label = "Beneficiary name")
        Spacer(modifier = Modifier.height(12.dp))

        // Amount and Currency Row
        Row(modifier = Modifier.fillMaxWidth()) {
            PaymentInputField(
                label = "Amount",
                isRequired = true,
                value = "0,00",
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
    minHeight: androidx.compose.ui.unit.Dp = 56.dp,
    alignTop: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(minHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(InputBackground)
            .clickable { /* Focus input */ }
            .padding(horizontal = 16.dp, vertical = if (alignTop) 16.dp else 0.dp),
        contentAlignment = if (alignTop) Alignment.TopStart else Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
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
                fontSize = 16.sp
            )

            // Right side Content (Value or Icon)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (value.isNotEmpty()) {
                    Text(
                        text = value,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (trailingIcon != null) {
                    if (value.isNotEmpty()) Spacer(modifier = Modifier.width(12.dp))
                    trailingIcon()
                }
            }
        }
    }
}