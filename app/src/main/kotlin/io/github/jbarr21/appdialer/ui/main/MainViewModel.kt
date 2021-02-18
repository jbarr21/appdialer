package io.github.jbarr21.appdialer.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.data.asText
import io.github.jbarr21.appdialer.util.Trie
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
  private val appRepo: AppRepo
) : ViewModel() {

  private val allApps by lazy { MutableLiveData<List<App>>() }
  private val query = mutableStateOf(listOf<DialerButton>())
  private val trie = Trie<App>()

  val buttonColors = listOf(0xff2196f3, 0xfff44336, 0xffffeb3b, 0xff4caf50, 0xff888888).map { Color(it) }
  val selectedApp = mutableStateOf<App?>(null)
  val filteredApps by lazy { MutableLiveData<List<App>>() }
  val queryText = mutableStateOf(query.value.asText())

  init {
    loadApps(useCache = true)
  }

  fun addToQuery(dialerButton: DialerButton) {
    query.value = query.value.plus(dialerButton)
    queryText.value = query.value.asText()
    trie.predictWord(queryText.value)
      .sortedBy { it.label.toLowerCase() }
      .also { apps -> filteredApps.value = apps }
  }

  fun clearQuery() {
    query.value = emptyList()
    filteredApps.value = allApps.value
  }

  fun loadApps(useCache: Boolean = true) {
    viewModelScope.launch {
      appRepo.loadApps(useCache).let { apps ->
        allApps.value = apps
        if (query.value.isEmpty()) {
          filteredApps.value = apps
        }
        trie.clear()
        apps.forEach { trie.add(it.label, it) }
      }
    }
  }

  class Factory @Inject constructor(private val appRepo: AppRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MainViewModel(appRepo) as T
  }
}