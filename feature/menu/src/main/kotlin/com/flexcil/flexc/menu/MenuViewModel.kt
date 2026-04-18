package com.flexcil.flexc.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexcil.flexc.core.audio.AppAudioManager
import com.flexcil.flexc.core.audio.Music
import com.flexcil.flexc.core.datastore.preference.UserPreferences
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val userPreferencesUseCase: UserPreferencesUseCase,
    private val gamePreferencesUseCase: GamePreferencesUseCase,
    private val appAudioManager: AppAudioManager,
    private val navigator: Navigator
) : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()



    fun play() {
        appAudioManager.playMusic(Music.Menu)
    }

    fun pause() {
        appAudioManager.pausePlayMusic()
    }


    fun navigateToGame() {
        navigator.launchScreen(AppScreen.GameScreen)
    }

    fun getPreferences() {
        getUserPreferences()
        getGamePreferences()
    }

    fun updateUserPreferences(userPreferences: UserPreferences) =
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesUseCase.updateUserPreferences(userPreferences).fold(
                onSuccess = { _state.update { it.copy(userPreferences = userPreferences) } },
                onFailure = { e -> e.printStackTrace() }
            )
        }

    private fun getUserPreferences() = viewModelScope.launch(Dispatchers.IO) {
        userPreferencesUseCase.get().let {
            _state.update { state ->
                state.copy(userPreferences = it)
            }
        }
    }

    private fun getGamePreferences() = viewModelScope.launch(Dispatchers.IO) {
        gamePreferencesUseCase.get().let {
            _state.update { state ->
                state.copy(gamePreferences = it)
            }
        }
    }
}