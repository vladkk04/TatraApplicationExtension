package com.flexcil.flexc.menu.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import com.flexcil.flexc.feature.menu.R
import com.flexcil.flexc.core.ui.component.AppButton
import com.flexcil.flexc.core.ui.component.AppDialog

@Composable
fun StatisticsDialog(
    highScore: Int,
    onDismiss: () -> Unit = {},
) {
    AppDialog(
        onDismiss = onDismiss,
        isDismissible = true,
        titleContent = {
            Text(
                text = stringResource(R.string.statistics),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        content = {
            Text(
                text = "High highScore: $highScore",
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        bottomContent = {
            AppButton(
                text = "Close",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )

}