package io.github.jbarr21.appdialer.ui.apps

import com.jakewharton.rxrelay2.PublishRelay
import io.github.jbarr21.appdialer.data.App

class AppClickStream {
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
