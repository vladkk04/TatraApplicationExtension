package com.flexcil.flexc.menu.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.flexcil.flexc.feature.menu.R
import com.flexcil.flexc.core.ui.component.AppButton
import com.flexcil.flexc.core.ui.component.AppDialog

@Composable
fun HowToPlayDialog(
    onDismiss: () -> Unit
) {
    AppDialog(
        usePlatformDefaultWidth = false,
        titleContent = {
            Text(
                text = stringResource(R.string.how_to_play),
                style = MaterialTheme.typography.titleSmall
            )
        },
        content = {
            Text(
                text = "Click on the safes and dodge the cards",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        bottomContent = {
            AppButton(
                text = "Close",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        },
        isDismissible = true,
        onDismiss = onDismiss
    )
}