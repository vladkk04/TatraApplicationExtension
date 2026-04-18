package com.flexcil.flexc.core.ui.component

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

@Composable
fun VideoPlayer(
    @RawRes raw: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                val resourceName = context.resources.getResourceEntryName(raw)

                val uri = "android.resource://${context.packageName}/raw/$resourceName".toUri()

                setVideoURI(uri)
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    start()
                }
            }
        },
        modifier = modifier
    )
}