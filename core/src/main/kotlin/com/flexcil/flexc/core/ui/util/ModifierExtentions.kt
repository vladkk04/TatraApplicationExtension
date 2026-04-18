package com.flexcil.flexc.core.ui.util

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

fun Modifier.detectHold(
    onHolding: () -> Unit = {},
    onCancelled: () -> Unit = {}
): Modifier = composed {
    val scope = rememberCoroutineScope()
    pointerInput(onHolding) {
        awaitEachGesture {
            val initialDown = awaitFirstDown(requireUnconsumed = false)
            val initialTouchHeldJob = scope.launch {
                while (initialDown.pressed) {
                    withFrameMillis { onHolding() }
                }
            }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
            onCancelled()
        }
    }
}