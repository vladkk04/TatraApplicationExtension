package com.flexcil.flexc.core.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    isDismissible: Boolean = false,
    content: @Composable () -> Unit,
) {
    if (!isVisible) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        sheetGesturesEnabled = isDismissible,
        modifier = modifier,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = isDismissible,
            shouldDismissOnClickOutside = isDismissible,
        )
    ) { content() }
}