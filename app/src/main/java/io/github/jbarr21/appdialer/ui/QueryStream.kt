package io.github.jbarr21.appdialer.ui

import com.google.common.base.Optional
import com.jakewharton.rxrelay2.BehaviorRelay
import io.github.jbarr21.appdialer.ui.dialer.DialerButton
import io.reactivex.Observable

class QueryStream {
  private val queryRelay = BehaviorRelay.createDefault(emptyList<DialerButton>())
  private val colorRelay = BehaviorRelay.createDefault(Optional.absent<Int>())

  fun query(): Observable<List<DialerButton>> = queryRelay.hide()

  fun color(): Observable<Optional<Int>> = colorRelay.hide()

  fun setQuery(query: List<DialerButton>) = queryRelay.accept(query)

  fun setColor(color: Optional<Int>) = colorRelay.accept(color)

  fun currentQuery() = queryRelay.value!!

  fun currentColor() = colorRelay.value!!
}
