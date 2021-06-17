package io.github.jbarr21.appdialer.ui.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.isMain
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(private val appRepo: AppRepo) : ViewModel() {

  var appStats by mutableStateOf(AppStats(-1, -1, -1, -1)).also { loadApps() }

  private fun loadApps() {
    viewModelScope.launch {
      val apps = appRepo.loadApps(true)
      appStats = AppStats(
        totalCount = apps.size,
        uniqueAppCount = apps.groupBy { it.packageName + it.activityName }.size,
        mainAppCount = apps.filter { it.user.isMain }.size,
        workAppCount = apps.filter { !it.user.isMain }.size
      )
    }
  }
}

data class AppStats(
  val totalCount: Int,
  val uniqueAppCount: Int,
  val mainAppCount: Int,
  val workAppCount: Int
) {
  val isLoaded = totalCount >= 0
}
