package com.flexcil.flexc.initial

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flexcil.flexc.feature.initial.R
import com.flexcil.flexc.core.ui.component.AppLoadingProgressText
import com.flexcil.flexc.core.ui.component.VideoPlayer
import com.flexcil.flexc.core.ui.util.ActivityOrientation
import com.flexcil.flexc.core.ui.util.changeOrientation

@Composable
fun InitialScreen() {
    val viewModel = hiltViewModel<InitialViewModel>()

    val activity = LocalActivity.current
    activity?.changeOrientation(ActivityOrientation.PORTRAIT)


    InitialContent()
}

@Composable
private fun InitialContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AppLoadingProgressText(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter),
        )
    }
}

