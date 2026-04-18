package com.flexcil.flexc.qrScanner

import androidx.lifecycle.ViewModel
import com.flexcil.flexc.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QrScannerViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    private val _scannedResult = MutableStateFlow<String?>(null)
    val scannedResult: StateFlow<String?> = _scannedResult.asStateFlow()

    fun onQrScanned(result: String) {
        if (_scannedResult.value != result) {
            _scannedResult.value = result
        }
    }

    fun resetScanner() {
        _scannedResult.value = null
    }

    fun closeScanner() {
        navigator.goBack()
    }
}