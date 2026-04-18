package com.flexcil.flexc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.initial.InitialScreen
import com.flexcil.flexc.navigation.base.AppNavigator

@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
) {
    val navigator = AppNavigator.get()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry(AppScreen.InitialScreen) { InitialScreen() }
        },
        modifier = modifier
    )
}