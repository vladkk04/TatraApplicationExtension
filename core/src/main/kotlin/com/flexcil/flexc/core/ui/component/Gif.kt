package com.flexcil.flexc.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun AppImageGif(
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val request = remember {
        ImageRequest.Builder(context)
            .data(drawableResId)
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = request,
        contentDescription = "Local GIF",
        modifier = modifier
    )
}