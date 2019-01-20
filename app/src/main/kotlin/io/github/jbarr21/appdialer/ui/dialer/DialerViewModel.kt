package io.github.jbarr21.appdialer.ui.dialer

import androidx.lifecycle.ViewModel
import io.github.jbarr21.appdialer.util.Trie
import io.github.jbarr21.appdialer.data.App

class DialerViewModel : ViewModel() {
  val query = mutableListOf<DialerButton>()
  val trie = Trie<App>()
}
