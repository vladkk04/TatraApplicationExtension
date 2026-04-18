package com.flexcil.flexc.savingGroups

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

private val ChartGreen = Color(0xFF66E289)
private val AvatarRed = Color(0xFFE55D5D)
private val AvatarOlive = Color(0xFFD2BCA0)
private val AvatarPurple = Color(0xFF2B88F0)
private val AvatarRose = Color(0xFFFBCFE8)

private val DarkCardBorder = Color(0xFF2F3036)
private val TabBackground = Color(0xFF1C1C1E)
private val TabActiveBackground = Color(0xFF2C2C2E)

data class Contributor(
    val id: String,
    val initials: String,
    val name: String,
    val amount: Double,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGroupsDetailsScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val incomeContributors = remember {
        listOf(
            Contributor("1", "VD", "Vladyslav Dorosh", 1700.0, AvatarRed),
            Contributor("2", "VK", "Vladyslav Klymiuk", 900.0, AvatarOlive),
            Contributor("3", "DD", "Daniil Dryzhov", 1100.0, AvatarPurple),
            Contributor("4", "DY", "Danyil Yatluk", 700.0, AvatarRose)
        )
    }

    val expenseContributors = remember {
        listOf(
            Contributor("1", "VD", "Vladyslav Dorosh", -500.0, AvatarRed),
            Contributor("2", "VK", "Vladyslav Klymiuk", -1200.0, AvatarOlive),
            Contributor("3", "DD", "Daniil Dryzhov", -300.0, AvatarPurple),
            Contributor("4", "DY", "Danyil Yatluk", -400.0, AvatarRose)
        )
    }

    val currentContributors = if (selectedTab == 0) incomeContributors else expenseContributors

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Group Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            SavingsOverviewCard(currentContributors, isExpense = selectedTab == 1)
            Spacer(modifier = Modifier.height(24.dp))
            SavingsTabs(selectedTab) { selectedTab = it }
            Spacer(modifier = Modifier.height(24.dp))
            RequestsSection()
            Spacer(modifier = Modifier.height(32.dp))
            ContributionsSection(currentContributors, isExpense = selectedTab == 1)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SavingsOverviewCard(contributors: List<Contributor>, isExpense: Boolean) {
    val totalAmount = contributors.sumOf { it.amount }
    var selectedContributor by remember { mutableStateOf<Contributor?>(null) }
    // Reset selection when tab changes
    LaunchedEffect(isExpense) { selectedContributor = null }

    val displayColor = if (isExpense) Color(0xFFE55D5D) else ChartGreen

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF1C1C1E),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        if (isExpense) "Total Expenses" else "Your Savings",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "${totalAmount.toInt()} EUR",
                        color = if (isExpense) Color(0xFFE55D5D) else Color.White,
                        fontSize = 40.sp,
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
                        .background(Color.White.copy(alpha = 0.1f))
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

                // Popup Tooltip
                selectedContributor?.let { contributor ->
                    Popup(
                        alignment = Alignment.TopCenter,
                        offset = androidx.compose.ui.unit.IntOffset(0, -100),
                        onDismissRequest = { selectedContributor = null },
                        properties = PopupProperties(focusable = false)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF2C2C2E),
                            border = BorderStroke(1.dp, contributor.color.copy(alpha = 0.5f)),
                            shadowElevation = 8.dp
                        ) {
                            Text(
                                text = "${contributor.name}: ${contributor.amount.toInt()} EUR",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
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
    val chartColor = if (isExpense) Color(0xFFE55D5D) else ChartGreen
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
        color = TabBackground
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
                        .background(if (selected) TabActiveBackground else Color.Transparent)
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = if (selected) Color.White else Color.White.copy(alpha = 0.5f),
                        fontSize = 15.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ContributionsSection(contributors: List<Contributor>, isExpense: Boolean) {
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
                modifier = Modifier.clickable { }
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
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1C1C1E),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
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
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "SK30 **** **** 3664",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 12.sp
                )
            }
            Text(
                "${contributor.amount.toInt()} EUR",
                color = if (isExpense) Color(0xFFE55D5D) else ChartGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RequestsSection() {
    var isApproved by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Requests",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1C1C1E),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isApproved) Color.Gray.copy(alpha = 0.2f) else AvatarRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("VD", color = if (isApproved) Color.Gray else AvatarRed, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Vladyslav Dorosh",
                        color = if (isApproved) Color.Gray else Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("1200 EUR", color = if (isApproved) Color.Gray else Color(0xFFE55D5D), fontSize = 14.sp)
                }
                
                if (isApproved) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.05f),
                        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
                    ) {
                        Text(
                            "Approved 1/3",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { isApproved = true },
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
}
