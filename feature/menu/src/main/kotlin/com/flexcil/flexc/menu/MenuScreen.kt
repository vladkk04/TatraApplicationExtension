package com.flexcil.flexc.menu

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flexcil.flexc.feature.menu.R
import com.flexcil.flexc.menu.component.HowToPlayDialog
import com.flexcil.flexc.menu.component.LevitationText
import com.flexcil.flexc.menu.component.SettingsDialog
import com.flexcil.flexc.menu.component.StatisticsDialog
import com.flexcil.flexc.core.ui.component.AppIconButton
import com.flexcil.flexc.core.ui.util.ActivityOrientation
import com.flexcil.flexc.core.ui.util.changeOrientation

@Composable
fun MenuScreen() {
    val viewModel = hiltViewModel<MenuViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val activity = LocalActivity.current

    var isVisibleSettingsDialog by remember { mutableStateOf(false) }
    var isVisibleHowToPlayDialog by remember { mutableStateOf(false) }
    var isVisibleStatisticsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        activity?.changeOrientation(ActivityOrientation.LANDSCAPE)
        viewModel.getPreferences()
    }

    LifecycleResumeEffect(Unit) {
        viewModel.play()
        onPauseOrDispose {/* viewModel.pause()*/ }
    }

    if (isVisibleSettingsDialog) {
        SettingsDialog(
            userPreferences = state.userPreferences,
            onDismiss = { isVisibleSettingsDialog = false },
            onApply = viewModel::updateUserPreferences
        )
    }

    if (isVisibleHowToPlayDialog) {
        HowToPlayDialog(
            onDismiss = { isVisibleHowToPlayDialog = false }
        )
    }

    if (isVisibleStatisticsDialog) {
        StatisticsDialog(
            highScore = state.gamePreferences.score,
            onDismiss = { isVisibleStatisticsDialog = false }
        )
    }

    MenuContent(
        onPlayClick = viewModel::navigateToGame,
        onSettingsClick = { isVisibleSettingsDialog = true },
        onHowToPlayClick = { isVisibleHowToPlayDialog = true },
        score = state.gamePreferences.score
    )
}

@Composable
private fun MenuContent(
    onPlayClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHowToPlayClick: () -> Unit = {},
    score: Int
) {

    Box(
        Modifier
            .fillMaxSize()
            .paint(painterResource(R.drawable.menu), contentScale = ContentScale.Crop)
            .clickable(
                onClick = onPlayClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            LevitationText()
            Text(
                text = "High score: $score",
                style = MaterialTheme.typography.bodySmall
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(88.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.BottomCenter)
        ) {
            AppIconButton(
                onClick = onSettingsClick,
                iconSize = 32.dp,
                imageVector = Icons.Default.Settings
            )

            AppIconButton(
                onClick = onHowToPlayClick,
                iconSize = 32.dp,
                imageVector = Icons.Default.QuestionMark
            )
        }
    }


}