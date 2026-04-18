package com.flexcil.flexc.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexcil.flexc.core.navigation.AppScreen
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val navigator: Navigator
): ViewModel() {


}