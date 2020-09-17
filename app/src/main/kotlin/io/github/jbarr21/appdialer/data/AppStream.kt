package io.github.jbarr21.appdialer.data

import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class AppStream @Inject constructor() {
  private val appChannel = BroadcastChannel<List<App>>(Channel.CONFLATED)
  private var currentApps = emptyList<App>()

  fun allApps() = appChannel.asFlow()

  fun setApps(apps: List<App>, coroutineScope: CoroutineScope) = coroutineScope.launch {
    currentApps = apps
    appChannel.send(apps)
  }

  fun currentApps() = currentApps
}