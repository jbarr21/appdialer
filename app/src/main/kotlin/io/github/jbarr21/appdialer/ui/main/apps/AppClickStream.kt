package io.github.jbarr21.appdialer.ui.main.apps

import dagger.hilt.android.scopes.ActivityScoped
import io.github.jbarr21.appdialer.data.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class AppClickStream @Inject constructor() {

  private val clickChannel = BroadcastChannel<App>(BUFFERED)
  private val longClickChannel = BroadcastChannel<App>(BUFFERED)

  fun clicks() = clickChannel.asFlow()

  fun longClicks() = longClickChannel.asFlow()

  fun onClick(app: App, coroutineScope: CoroutineScope) = coroutineScope.launch {
    clickChannel.send(app)
  }

  fun onLongClick(app: App, coroutineScope: CoroutineScope) = coroutineScope.launch {
    longClickChannel.send(app)
  }
}