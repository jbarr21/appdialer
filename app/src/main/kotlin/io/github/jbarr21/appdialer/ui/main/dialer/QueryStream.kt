package io.github.jbarr21.appdialer.ui.main.dialer

import com.google.common.base.Optional
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import javax.inject.Inject

class QueryStream @Inject constructor() {
  private val queryRelay = BehaviorRelay.createDefault(emptyList<DialerButton>())
  private val colorRelay = BehaviorRelay.createDefault(Optional.absent<Int>())
  private val longClicksRelay = PublishRelay.create<DialerButton>()

  fun query(): Observable<List<DialerButton>> = queryRelay.hide()

  fun color(): Observable<Optional<Int>> = colorRelay.hide()

  fun longClicks() = longClicksRelay.hide()

  fun setQuery(query: List<DialerButton>) = queryRelay.accept(query)

  fun setColor(color: Optional<Int>) = colorRelay.accept(color)

  fun currentQuery() = queryRelay.value!!

  fun currentColor() = colorRelay.value!!

  fun longClick(button: DialerButton) = longClicksRelay.accept(button)
}
