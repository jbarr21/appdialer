package io.github.jbarr21.appdialer.ui.main

import android.app.Activity
import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.data.asText
import io.github.jbarr21.appdialer.ui.Screen
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.Trie
import io.github.jbarr21.appdialer.util.Vibrator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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
      .sortedBy { it.label.toLowerCase() }
      .also { apps -> filteredApps = apps }
  }

  fun clearQuery() {
    query = emptyList()
    filteredApps = allApps
  }

  fun loadApps(useCache: Boolean = true) {
    isRefreshing = true
    viewModelScope.launch {
      appRepo.loadApps(useCache).let { apps ->
        allApps = apps
        if (query.isEmpty()) {
          filteredApps = apps
        }
        trie.clear()
        apps.forEach { trie.add(it.label, it) }
        delay(3000)
        isRefreshing = false
      }
    }
  }

  fun onAppClicked(context: Context, app: App) {
    vibrator.vibrate()
    activityLauncher.startMainActivity(app)
    (context as Activity).finishAndRemoveTask()
  }

  @ExperimentalMaterialApi
  fun onAppLongClicked(app: App?, sheetState: ModalBottomSheetState): App? {
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

  fun onDialerLongClicked(button: DialerButton, navController: NavController) {
    vibrator.vibrate()
    if (button.isClearButton) {
      navController.navigate(Screen.Settings.toString())
    } else if (button.digit == 7) {
      activityLauncher.startShowkase()
    }
  }

  fun onDialerLongClickedDecorated(button: DialerButton, navController: NavController) {
    onDialerLongClicked(button, navController)
    if (button.isInfoButton) {
      navController.navigate(Screen.Info.toString())
    }
  }
}
