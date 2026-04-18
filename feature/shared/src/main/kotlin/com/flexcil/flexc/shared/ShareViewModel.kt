package com.flexcil.flexc.shared

import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {

    fun navigateToQrScanner() {
        navigator.launchScreen(AppScreen.QrScanner)
    }

    fun navigateToCreateGroud() {
        navigator.launchScreen(AppScreen.NewGroup)
    }
}