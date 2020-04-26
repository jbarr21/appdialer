package io.github.jbarr21.appdialer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.app.AppScope
import io.github.jbarr21.appdialer.app.AppScopeImpl

class BootCompletedReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val appScope: AppScope = AppDialerApplication.component(context)
    KeepAliveService.start(context, appScope.sharedPreferences())
  }
}
