package com.flexcil.flexc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.flexcil.flexc.contribute.screen.PaymentScreen
import com.flexcil.flexc.contribute.screen.TransactionScreen
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.createGroup.screen.CreateGroupScreen
import com.flexcil.flexc.createGroup.screen.CreateSpendingScreen
import com.flexcil.flexc.debtGroups.screen.DepDetailsScreen
import com.flexcil.flexc.home.InitialScreen
import com.flexcil.flexc.navigation.base.AppNavigator
import com.flexcil.flexc.qrCreator.QrCreatorScreen
import com.flexcil.flexc.qrScanner.QrScannerScreen
import com.flexcil.flexc.savingGroups.SavingsGroupsDetailsScreen
import com.flexcil.flexc.shared.SharedScreen

@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
    backStack: (NavBackStack<AppScreen>) -> Unit
) {
    val navigator = AppNavigator.get()

    backStack(navigator.backStack)

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry(AppScreen.InitialScreen) { InitialScreen() }
            entry(AppScreen.SharedScreen) { SharedScreen() }
            entry(AppScreen.QrScanner) { QrScannerScreen() }
            entry(AppScreen.NewGroup) { CreateGroupScreen() }
            entry(AppScreen.SavingGroupsDetails) { SavingsGroupsDetailsScreen() }
            entry(AppScreen.QrCreator) { QrCreatorScreen() }
            entry(AppScreen.DebDetails) { DepDetailsScreen()  }
            entry(AppScreen.PaymentScreen) { PaymentScreen() }
            entry(AppScreen.TransactionScreen) { TransactionScreen() }
            entry(AppScreen.CreateGroupScreen) { CreateGroupScreen() }
            entry(AppScreen.CreateSpendingScreen) { CreateSpendingScreen() }
        },
        modifier = modifier
    )
}