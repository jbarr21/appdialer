package io.github.jbarr21.appdialer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

  @Inject
  lateinit var sharedPreferences: SharedPreferences

  override fun onReceive(context: Context, intent: Intent) {
    KeepAliveService.start(context, sharedPreferences)
  }
}
