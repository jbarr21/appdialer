package io.github.jbarr21.appdialer.ui.main.dialer

import com.google.common.base.Optional
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScoped
class QueryStream @Inject constructor() {
  private var currentQuery = emptyList<DialerButton>()
  private val queryChannel = BroadcastChannel<List<DialerButton>>(Channel.CONFLATED)

  private val colorChannel = BroadcastChannel<Optional<Int>>(Channel.CONFLATED)

  private val longClickChannel = BroadcastChannel<DialerButton>(Channel.BUFFERED)

  fun query() = queryChannel.asFlow()

  fun color() = colorChannel.asFlow()

  fun longClicks() = longClickChannel.asFlow()

  fun setQuery(query: List<DialerButton>, coroutineScope: CoroutineScope) = coroutineScope.launch {
    currentQuery = query
    queryChannel.send(query)
  }

  fun setColor(color: Optional<Int>, coroutineScope: CoroutineScope) = coroutineScope.launch {
    colorChannel.send(color)
  }

  fun longClick(button: DialerButton, coroutineScope: CoroutineScope) = coroutineScope.launch {
    longClickChannel.send(button)
  }

  fun currentQuery() = currentQuery
}