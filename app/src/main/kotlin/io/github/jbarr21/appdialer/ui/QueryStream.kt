package io.github.jbarr21.appdialer.ui

import com.google.common.base.Optional
import io.github.jbarr21.appdialer.ui.dialer.DialerButton
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class QueryStream {
  private val querySubject = BehaviorSubject.createDefault(emptyList<DialerButton>())
  private val colorSubject = BehaviorSubject.createDefault(Optional.absent<Int>())

  fun query(): Observable<List<DialerButton>> = querySubject.hide()

  fun color(): Observable<Optional<Int>> = colorSubject.hide()

  fun setQuery(query: List<DialerButton>) = querySubject.onNext(query)

  fun setColor(color: Optional<Int>) = colorSubject.onNext(color)

  fun currentQuery() = querySubject.value!!

  fun currentColor() = colorSubject.value!!
}
