package io.github.jbarr21.appdialer.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.jbarr21.appdialer.data.UserPreferences
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel(private val userPreferencesRepo: UserPreferencesRepo) : ViewModel() {

  val userPreferences by lazy {
    MutableLiveData<UserPreferences>().also {
      loadUserPreferences()
    }
  }

  private fun loadUserPreferences() {
    viewModelScope.launch {
      userPreferencesRepo.userPreferencesFlow
        .collect { userPreferences.value = it }
    }
  }

  fun updateUseHaptipFeedback(enable: Boolean) = viewModelScope.launch {
    userPreferencesRepo.updateUseHaptipFeedback(enable)
  }

  fun updateUsePersistentService(enable: Boolean) = viewModelScope.launch {
    userPreferencesRepo.updateUsePersistentService(enable)
  }

  class Factory @Inject constructor(private val userPreferencesRepo: UserPreferencesRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = SettingsViewModel(userPreferencesRepo) as T
  }
}