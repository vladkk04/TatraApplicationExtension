package com.flexcil.flexc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.flexcil.flexc.app.component.BottomNavigationBar
import com.flexcil.flexc.app.component.TopBar
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.ui.theme.ApplicationTheme
import com.flexcil.flexc.debtGroups.screen.DepDetailsScreen
import com.flexcil.flexc.debtGroups.screen.MeDebGroupScreen
import com.flexcil.flexc.navigation.AppNavDisplay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            var visibleBars by remember { mutableStateOf(true) }
            var changeTopBar by remember { mutableStateOf(false) }

            ApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (visibleBars) {
                            TopBar(changeTopBar)
                        }
                    },
                    bottomBar = {
                        if (visibleBars) {
                            BottomNavigationBar(
                                onHomeClick = viewModel::navigateHome,
                                onSharedClick = viewModel::navigateShared
                            )
                        }
                    }
                ) { contentPadding ->
                    Box(
                        Modifier.fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        DepDetailsScreen()

                    }
                   /* AppNavDisplay(
                        backStack = { stack ->
                            visibleBars = !stack.contains(AppScreen.QrScanner)
                            changeTopBar = stack.contains(AppScreen.SharedScreen)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    )*/
                }
            }
        }
    }
}



