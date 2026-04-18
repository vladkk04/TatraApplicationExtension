package com.flexcil.flexc.savingGroups

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Specific Data Colors from Design ---
private val ChartGreen = Color(0xFF66E289)
private val AvatarRed = Color(0xFF6B1E2C)
private val AvatarOlive = Color(0xFF4C5D23)
private val AvatarPurple = Color(0xFF2E235D)
private val AvatarRose = Color(0xFFC08985)
private val SegmentRed = Color(0xFFB91C1C)
private val SegmentBlue = Color(0xFF1D4ED8)
private val SegmentGreen = Color(0xFF4ADE80)
private val SegmentPink = Color(0xFFFBCFE8)
private val DarkCardBorder = Color(0xFF2F3036)
private val TabBackground = Color(0xFF232429)
private val TabActiveBackground = Color(0xFF3B3C42)

@Composable
fun SavingsGroupsDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        SavingsOverviewCard(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(24.dp))

        SavingsTabs(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(24.dp))

        ContributionsSection(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(32.dp))

        RequestsSection(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(40.dp)) // Bottom padding
    }
}

@Composable
private fun SavingsOverviewCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side (Text & Amounts)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Your Savings",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif // Similar to the elegant font in design
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "4400$",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Color Segments Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Box(modifier = Modifier.weight(0.35f).fillMaxSize().background(SegmentRed))
                    Box(modifier = Modifier.weight(0.35f).fillMaxSize().background(SegmentBlue))
                    Box(modifier = Modifier.weight(0.2f).fillMaxSize().background(SegmentGreen))
                    Box(modifier = Modifier.weight(0.1f).fillMaxSize().background(SegmentPink))
                }
            }

            // Right side (Chart)
            SavingsChart(
                modifier = Modifier
                    .width(140.dp)
                    .height(80.dp)
            )
        }
    }
}

@Composable
private fun SavingsChart(modifier: Modifier = Modifier) {
    val gridColor = MaterialTheme.colorScheme.surfaceVariant

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Draw horizontal grid lines
        val linesCount = 4
        val spacing = height / linesCount
        for (i in 0..linesCount) {
            val y = i * spacing
            drawLine(
                color = gridColor.copy(alpha = 0.5f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Define chart path (imitating the curve from the image)
        val path = Path().apply {
            moveTo(0f, height * 0.9f)
            cubicTo(width * 0.2f, height * 0.5f, width * 0.3f, height * 0.8f, width * 0.5f, height * 0.6f)
            cubicTo(width * 0.7f, height * 0.4f, width * 0.8f, height * 0.4f, width, height * 0.1f)
        }

        // Fill path for gradient
        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(ChartGreen.copy(alpha = 0.3f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        // Draw green line
        drawPath(
            path = path,
            color = ChartGreen,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
private fun SavingsTabs(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(TabBackground)
            .padding(4.dp)
    ) {
        TabItem(
            text = "Incomes",
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            modifier = Modifier.weight(1f)
        )
        TabItem(
            text = "Expenses",
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) TabActiveBackground else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ContributionsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Contribute",
                color = MaterialTheme.colorScheme.primary, // Blue color from image
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Handle contribute */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        ContributorItem(initials = "VD", name = "Vladyslav Dorosh", amount = "+1700,00", avatarColor = AvatarRed)
        Spacer(modifier = Modifier.height(8.dp))
        ContributorItem(initials = "VK", name = "Vladyslav Klymiuk", amount = "+900,00", avatarColor = AvatarOlive)
        Spacer(modifier = Modifier.height(8.dp))
        ContributorItem(initials = "DD", name = "Daniil Dryzhov", amount = "+1100,00", avatarColor = AvatarPurple)
        Spacer(modifier = Modifier.height(8.dp))
        ContributorItem(initials = "DY", name = "Danyil Yatluk", amount = "+700,00", avatarColor = AvatarRose, isLast = true)
    }
}

@Composable
private fun ContributorItem(
    initials: String,
    name: String,
    amount: String,
    avatarColor: Color,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background) // Very dark background
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name and Card Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "SK30 **** **** **** 3664",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        // Amount
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = amount,
                color = ChartGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " EUR",
                color = ChartGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
            )
        }
    }
}

@Composable
private fun RequestsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Requests",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AvatarRed),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VD",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Vladyslav Dorosh",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "-1200 $",
                    color = MaterialTheme.colorScheme.error, // Red text for negative request
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Approve Button
            Button(
                onClick = { /* Approve action */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Blue button
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Text(text = "Approve", fontSize = 14.sp)
            }
        }
    }
}