package com.flexcil.flexc.navigation.base

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AppNavigator @Inject constructor() : Navigator {
    val backStack: NavBackStack<AppScreen> = NavBackStack(AppScreen.TransactionScreen)

    override fun launchScreen(screen: AppScreen) {
        if(!backStack.contains(screen)) {
            backStack.add(screen)
        }
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }

    @HiltViewModel
    class VM @Inject constructor(
        val navigator: AppNavigator
    ) : ViewModel()

    companion object {
        @Composable
        fun get(): AppNavigator = hiltViewModel<VM>().navigator
    }
}
