package io.github.jbarr21.appdialer.ui.main

import android.app.Activity
import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.Inject
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.data.asText
import io.github.jbarr21.appdialer.ui.InfoCircuitScreen
import io.github.jbarr21.appdialer.ui.SettingsCircuitScreen
import com.slack.circuit.runtime.screen.Screen
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.Trie
import io.github.jbarr21.appdialer.util.Vibrator
import kotlinx.coroutines.delay
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel @Inject constructor(
  private val activityLauncher: ActivityLauncher,
  internal val appLongClickActions: List<SimpleListItem<App>>,
  private val appRepo: AppRepo,
  internal val dialerLabels: List<DialerButton>,
  private val vibrator: Vibrator
) : ViewModel() {

  private var allApps by mutableStateOf(emptyList<App>())
  private var query by mutableStateOf(listOf<DialerButton>())
  private var trie = Trie<App>()
  private var loadAppsJob: Job? = null

  val buttonColors = listOf(0xff2196f3, 0xfff44336, 0xffffeb3b, 0xff4caf50, 0xff888888).map { Color(it) }
  var selectedApp = mutableStateOf<App?>(null)
  var filteredApps by mutableStateOf(emptyList<App>())
  var queryText by mutableStateOf(query.asText())
  var isRefreshing by mutableStateOf(false)

  init {
    loadApps(useCache = true)
  }

  fun addToQuery(dialerButton: DialerButton) {
    query = query.plus(dialerButton)
    queryText = query.asText()
    trie.predictWord(queryText)
      .sortedBy { it.label.lowercase() }
      .also { apps -> filteredApps = apps }
  }

  fun clearQuery() {
    query = emptyList()
    filteredApps = allApps
  }

  fun loadApps(useCache: Boolean = true) {
    loadAppsJob?.cancel()
    isRefreshing = true
    loadAppsJob = viewModelScope.launch {
      try {
        appRepo.loadApps(useCache).let { apps ->
          allApps = apps
          if (query.isEmpty()) {
            filteredApps = apps
          }
          trie.clear()
          apps.forEach { trie.add(it.label, it) }
          delay(3000)
        }
      } finally {
        if (currentCoroutineContext().isActive) {
          isRefreshing = false
          loadAppsJob = null
        }
      }
    }
  }

  fun onAppClicked(context: Context, app: App) {
    vibrator.vibrate()
    activityLauncher.startMainActivity(app)
    (context as Activity).finishAndRemoveTask()
  }

  @ExperimentalMaterialApi
  fun onAppLongClicked(app: App?): App? {
    vibrator.vibrate()
    selectedApp.value = app
    return app
  }

  val onDialerClicked: (DialerButton) -> Unit = {
    vibrator.vibrate()
    if (it.isClearButton) {
      clearQuery()
    } else {
      addToQuery(it)
    }
  }

  fun onDialerLongClicked(button: DialerButton, navigate: (Screen) -> Unit) {
    vibrator.vibrate()
    if (button.isClearButton) {
      navigate(SettingsCircuitScreen)
    }
  }

  fun onDialerLongClickedDecorated(button: DialerButton, navigate: (Screen) -> Unit) {
    onDialerLongClicked(button, navigate)
    if (button.isInfoButton) {
      navigate(InfoCircuitScreen)
    }
  }
}
