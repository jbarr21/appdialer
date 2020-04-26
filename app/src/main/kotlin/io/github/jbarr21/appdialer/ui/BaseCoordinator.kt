package io.github.jbarr21.appdialer.ui

import android.view.View
import androidx.annotation.CallSuper
import com.jakewharton.rxrelay2.BehaviorRelay
import com.squareup.coordinators.Coordinator
import com.uber.autodispose.OutsideScopeException
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import io.reactivex.CompletableSource
import io.reactivex.Observable

abstract class BaseCoordinator : Coordinator(), LifecycleScopeProvider<CoordinatorEvent> {

  private val lifecycleRelay = BehaviorRelay.create<CoordinatorEvent>()

  private val CORRESPONDING_EVENTS = CorrespondingEventsFunction { lastEvent: CoordinatorEvent ->
    return@CorrespondingEventsFunction when (lastEvent) {
      CoordinatorEvent.ATTACH -> CoordinatorEvent.DETACH
      else -> throw OutsideScopeException("Coordinator is detached!")
    }
  }

  @CallSuper
  override fun attach(view: View) {
    super.attach(view)
    lifecycleRelay.accept(CoordinatorEvent.ATTACH)
  }

  @CallSuper
  override fun detach(view: View) {
    super.detach(view)
    lifecycleRelay.accept(CoordinatorEvent.DETACH)
  }

  override fun correspondingEvents(): CorrespondingEventsFunction<CoordinatorEvent> {
    return CORRESPONDING_EVENTS
  }

  override fun lifecycle(): Observable<CoordinatorEvent> {
    return lifecycleRelay.hide()
  }

  override fun peekLifecycle(): CoordinatorEvent? {
    return lifecycleRelay.value
  }

  override fun requestScope(): CompletableSource? {
    return LifecycleScopes.resolveScopeFromLifecycle(this)
  }
}