package io.github.jbarr21.appdialer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.jbarr21.appdialer.app.AppDialerApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val app = context.applicationContext as AppDialerApplication
    GlobalScope.launch(Dispatchers.Main) {
      KeepAliveService.start(context, app.appGraph.userPreferencesRepo)
    }
  }
}
