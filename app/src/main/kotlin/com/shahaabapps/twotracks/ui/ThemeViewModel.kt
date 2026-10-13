package com.shahaabapps.twotracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.datastore.ThemePreferences
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeModeName
        .map { runCatching { ThemeMode.valueOf(it) }.getOrDefault(ThemeMode.LIGHT) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.LIGHT)

    val look: StateFlow<AppLook> = themePreferences.lookName
        .map { runCatching { AppLook.valueOf(it) }.getOrDefault(AppLook.PARALLEL) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppLook.PARALLEL)

    fun setLook(look: AppLook) {
        viewModelScope.launch { themePreferences.setLookName(look.name) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { themePreferences.setThemeModeName(mode.name) }
    }
}
