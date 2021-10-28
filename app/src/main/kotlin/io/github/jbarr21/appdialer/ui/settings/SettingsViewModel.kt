package io.github.jbarr21.appdialer.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.data.UserPreferences
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val userPreferencesRepo: UserPreferencesRepo,
  internal val settingsData: List<SimpleListItem<Unit>>
) : ViewModel() {

  var userPreferences by mutableStateOf(UserPreferences())

  init {
    loadUserPreferences()
  }

  private fun loadUserPreferences() {
    viewModelScope.launch {
      userPreferencesRepo.userPreferencesFlow.collect { userPreferences = it }
    }
  }

  fun updateUseHaptipFeedback(enable: Boolean) = viewModelScope.launch {
    userPreferencesRepo.updateUseHaptipFeedback(enable)
  }

  fun updateUsePersistentService(enable: Boolean) = viewModelScope.launch {
    userPreferencesRepo.updateUsePersistentService(enable)
  }
}