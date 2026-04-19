package com.flexcil.flexc.pending

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Data Models ---
enum class GroupType(val title: String) {
    SHARED_EXPENSES("Debt Group"),
    SAVINGS("Savings Account")
}

data class Invitation(
    val id: String,
    val groupName: String,
    val inviterName: String,
    val type: GroupType,
    val icon: ImageVector
)

// --- Colors ---
private val AppBackground = Color(0xFF14151A)
private val CardBackground = Color(0xFF232429)
private val TatraBlue = Color(0xFF2B88F0)
private val TextGray = Color(0xFF8B8D98)
private val DarkCardBorder = Color(0xFF2F3036)

@Composable
fun PendingScreen(
) {
    val invitations = remember {
        mutableStateListOf(
            Invitation("1", "Trip to Paris", "Danyil Yatluk", GroupType.SHARED_EXPENSES, Icons.Default.Group),
            Invitation("2", "Emergency Fund", "Vladyslav Dorosh", GroupType.SAVINGS, Icons.Default.Savings),
            Invitation("3", "Friday Party", "Vladyslav Klymiuk", GroupType.SHARED_EXPENSES, Icons.Default.Group)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        // --- Content ---
        if (invitations.isEmpty()) {
            // Порожній стан (Empty State), коли всі заявки оброблені
            EmptyInvitationsState()
        } else {
            // Список заявок
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // key = { it.id } дуже важливо для правильної анімації/видалення елементів у Compose
                items(
                    items = invitations,
                    key = { it.id }
                ) { invitation ->
                    InvitationCard(
                        invitation = invitation,
                        onAccept = {
                            // Логіка прийняття (наприклад, API call), потім видаляємо з UI
                            invitations.remove(invitation)
                        },
                        onDecline = {
                            // Логіка відхилення, потім видаляємо з UI
                            invitations.remove(invitation)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun InvitationCard(
    invitation: Invitation,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Верхня частина: Іконка + Текст
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(TatraBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = invitation.icon,
                        contentDescription = null,
                        tint = TatraBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = invitation.groupName,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${invitation.inviterName} invited you",
                        color = TextGray,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = invitation.type.title,
                        color = TatraBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Нижня частина: Кнопки Accept / Decline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDecline,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppBackground, // Дуже темний фон для "Відхилити"
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Text("Decline", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TatraBlue, // Синій акцент для "Прийняти"
                        contentColor = Color.White
                    )
                ) {
                    Text("Accept", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EmptyInvitationsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircleOutline,
            contentDescription = null,
            tint = TatraBlue.copy(alpha = 0.5f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You're all caught up!",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "There are no pending group invitations at the moment.",
            color = TextGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(60.dp)) // Візуальний баланс
    }
}