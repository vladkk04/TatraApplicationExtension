package com.flexcil.flexc.core.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.Thumb
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppLoadingProgressText(
    modifier: Modifier = Modifier,
    textLoading: String = "Loading",
    cycleDuration: Int = 1000
) {
    val transition = rememberInfiniteTransition(label = "Dots Transition")

    val visibleDotsCount by transition.animateValue(
        initialValue = 0,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Visible Dots Count"
    )

    Text(
        text = textLoading + ".".repeat(visibleDotsCount),
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLoadingSliderBar(
    modifier: Modifier = Modifier,
    value: Float = 0f,
    thumb: @Composable (SliderState) -> Unit = {
        Thumb(
            interactionSource = remember { MutableInteractionSource() },
            colors = SliderDefaults.colors(),
            enabled = true
        )
    },
) {
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "sliderAnimation"
    )

    val state = rememberSliderState(valueRange = 0f..100f)

    Slider(
        modifier = modifier,
        state = state.apply {
            this.value = animatedValue
            this.onValueChange = {}
        },
        thumb = thumb,
        track = {
            SliderDefaults.Track(
                sliderState = state,
                modifier = Modifier.fillMaxWidth(),
                drawStopIndicator = {},
                thumbTrackGapSize = 0.dp
            )
        }
    )
}

