package com.flexcil.flexc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

            // Змінна тепер може бути null. За замовчуванням null (щоб показувати лого)
            var topBarTitle by remember { mutableStateOf<String?>(null) }
            var isBackButton by remember { mutableStateOf(false) }
            var showCustomize by remember { mutableStateOf(false) }

            ApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (visibleBars) {
                            TopBar(
                                title = topBarTitle,
                                isBackButton = isBackButton,
                                showCustomize = showCustomize,
                                onCustomizeClick = { /* Logic for customization */ }
                            )
                        }
                    },
                    bottomBar = {
                        if (visibleBars) {
                            BottomNavigationBar(
                                onHomeClick = viewModel::navigateHome,
                                onSharedClick = viewModel::navigateShared,
                                onPaymentClick = viewModel::navigatePayment,
                                onTransactionClick = viewModel::navigateTransaction
                            )
                        }
                    }
                ) { contentPadding ->
                    AppNavDisplay(
                        backStack = { stack ->
                            val currentScreen = stack.lastOrNull()

                            visibleBars = currentScreen != AppScreen.QrScanner

                            isBackButton = currentScreen != null &&
                                    currentScreen != AppScreen.InitialScreen &&
                                    currentScreen != AppScreen.TransactionScreen &&
                                    currentScreen != AppScreen.PaymentScreen &&
                                    currentScreen != AppScreen.SharedScreen

                            showCustomize = currentScreen == AppScreen.InitialScreen

                            topBarTitle = when (currentScreen) {
                                AppScreen.InitialScreen,
                                AppScreen.SharedScreen,
                                AppScreen.TransactionScreen,
                                AppScreen.PaymentScreen -> null

                                AppScreen.CreateSpendingScreen -> "Adding New SubGroup"
                                AppScreen.QrScanner -> "Scan QR"
                                else -> "Details" // Резервний заголовок для інших екранів
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    )
                }
            }
        }
    }
}