package io.github.jbarr21.appdialer.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.util.Trie
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
  private val appRepo: AppRepo
) : ViewModel() {

  private val query = mutableStateOf(listOf<DialerButton>())
  private val trie = Trie<App>()

  val selectedApp = mutableStateOf<App?>(null)

  val allApps by lazy {
    MutableLiveData<List<App>>().also {
      loadAllApps()
    }
  }

  val filteredApps by lazy {
    MutableLiveData<List<App>>().also {
      loadAllApps()
    }
  }

  fun addToQuery(dialerButton: DialerButton) {
    query.value = query.value.plus(dialerButton)
    val queryText = query.value.map { it.letters.first().toString() }.joinToString(separator = "")
    trie.predictWord(queryText)
      .sortedBy { it.label.toLowerCase() }
      .also { apps -> filteredApps.value = apps }
  }

  fun queryText(): State<String> {
    return mutableStateOf(query.value.map { it.letters.first().toString() }.joinToString(separator = ""))
  }

  fun clearQuery() {
    query.value = emptyList()
    filteredApps.value = allApps.value
  }

  fun selectApp(app: App) {
    selectedApp.value = app
  }

  fun deselectApp() {
    selectedApp.value = null
  }

  private fun loadAllApps() {
    viewModelScope.launch {
      appRepo.loadApps().let { apps ->
        allApps.value = apps
        filteredApps.value = apps
        apps.forEach { trie.add(it.label, it) }
      }
    }
  }

  class Factory @Inject constructor(private val appRepo: AppRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MainViewModel(appRepo) as T
  }
}