package io.github.jbarr21.appdialer.ui.main

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

  val query = mutableListOf<DialerButton>()
  val trie = Trie<App>()

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
    query += dialerButton
    val queryText = query.map { it.letters[0].toString() }.joinToString(separator = "")
    trie.predictWord(queryText)
      .sortedBy { it.label.toLowerCase() }
      .also { apps -> filteredApps.value = apps }
  }

  fun clearQuery() {
    query.clear()
    filteredApps.value = allApps.value
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