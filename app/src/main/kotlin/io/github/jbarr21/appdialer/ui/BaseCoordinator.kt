package io.github.jbarr21.appdialer.ui

import android.view.View
import androidx.annotation.CallSuper
import com.squareup.coordinators.Coordinator
import kotlinx.coroutines.CoroutineScope

abstract class BaseCoordinator(
  private val coroutineScope: CoroutineScope
) : Coordinator(), CoroutineScope by coroutineScope {

  @CallSuper
  override fun attach(view: View) {
    super.attach(view)
  }

  @CallSuper
  override fun detach(view: View) {
    super.detach(view)
  }
}