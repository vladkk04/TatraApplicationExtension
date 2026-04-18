package com.flexcil.flexc.menu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.flexcil.flexc.feature.menu.R
import com.flexcil.flexc.core.datastore.preference.UserPreferences
import com.flexcil.flexc.core.ui.component.AppButton
import com.flexcil.flexc.core.ui.component.AppDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    userPreferences: UserPreferences,
    onDismiss: () -> Unit,
    onApply: (UserPreferences) -> Unit
) {
    val musicVolumeSliderState = rememberSliderState(userPreferences.musicVolume)
    val soundVolumeSliderState = rememberSliderState(userPreferences.soundVolume)

    var isVibrateEnabled by remember { mutableStateOf(userPreferences.isVibrationEnabled) }
    var isDarkTheme by remember { mutableStateOf(userPreferences.isDarkTheme ?: false) }

    AppDialog(
        onDismiss = onDismiss,
        isDismissible = true,
        titleContent = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        content = {
            SettingsSlider(soundVolumeSliderState, Icons.AutoMirrored.Filled.VolumeUp)
            SettingsSlider(musicVolumeSliderState, Icons.Filled.MusicNote)

            Row {
                IconButton(
                    onClick = { isVibrateEnabled = !isVibrateEnabled },
                ) {
                    if (isVibrateEnabled) Icon(Icons.Default.Vibration, null) else Icon(
                        painterResource(
                            R.drawable.vibration_off
                        ), null
                    )
                }
            }
        },
        bottomContent = {
            AppButton(
                text = stringResource(R.string.apply),
                onClick = {
                    onApply(
                        UserPreferences(
                            musicVolume = musicVolumeSliderState.value,
                            soundVolume = soundVolumeSliderState.value,
                            isVibrationEnabled = isVibrateEnabled,
                            isDarkTheme = isDarkTheme
                        )
                    )
                    onDismiss()
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSlider(
    state: SliderState,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
        Slider(state)
    }
}