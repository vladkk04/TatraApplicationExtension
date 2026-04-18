package com.flexcil.flexc.savingGroups

import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavingGroupsViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {

    fun navigateToSavingGroupDetails() {
        navigator.launchScreen(AppScreen.SavingGroupsDetails)
    }
}