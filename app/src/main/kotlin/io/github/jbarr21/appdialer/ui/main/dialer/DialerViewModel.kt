package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.lifecycle.ViewModel
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.util.Trie

class DialerViewModel : ViewModel() {
  val query = mutableListOf<DialerButton>()
  val trie = Trie<App>()
}