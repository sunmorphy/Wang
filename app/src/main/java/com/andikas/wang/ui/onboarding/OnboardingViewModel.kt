package com.andikas.wang.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andikas.wang.data.local.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _theme = MutableStateFlow("SYSTEM")
    val theme: StateFlow<String> = _theme.asStateFlow()

    init {
        viewModelScope.launch {
            preferenceManager.theme.collect {
                _theme.value = it.ifEmpty { "SYSTEM" }
            }
        }
    }

    fun saveTheme(theme: String) {
        viewModelScope.launch {
            preferenceManager.saveTheme(theme)
        }
    }
}