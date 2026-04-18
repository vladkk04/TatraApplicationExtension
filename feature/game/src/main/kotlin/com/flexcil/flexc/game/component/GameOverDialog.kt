package com.flexcil.flexc.game.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flexcil.flexc.core.ui.component.AppButton
import com.flexcil.flexc.core.ui.component.AppDialog

@Composable
fun GameOverDialog(
    onBackToMenu: () -> Unit,
    score: Int
) {
    AppDialog(
        onDismiss = {},
        titleContent = {
            Text("Game over!!")
        },
        content = {
            Text("Your score: $score")
        },
        bottomContent = {
            AppButton(
                onClick = onBackToMenu,
                text = "Back to menu",
                modifier = Modifier.fillMaxWidth()
            )
        }
    )

}