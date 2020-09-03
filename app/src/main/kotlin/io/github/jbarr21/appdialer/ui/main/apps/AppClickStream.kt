package io.github.jbarr21.appdialer.ui.main.apps

import com.jakewharton.rxrelay2.PublishRelay
import io.github.jbarr21.appdialer.data.App
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppClickStream @Inject constructor() {
  private val clickRelay = PublishRelay.create<App>()
  private val longClickRelay = PublishRelay.create<App>()

  fun clicks() = clickRelay.hide()

  fun longClicks() = longClickRelay.hide()

  fun onClick(app: App) {
    clickRelay.accept(app)
  }

  fun onLongClick(app: App): Boolean {
    longClickRelay.accept(app)
    return true
  }
}
