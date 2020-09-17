package io.github.jbarr21.appdialer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

  @Inject
  lateinit var userPreferencesRepo: UserPreferencesRepo

  override fun onReceive(context: Context, intent: Intent) {
    GlobalScope.launch(Dispatchers.Main) {
      KeepAliveService.start(context, userPreferencesRepo)
    }
  }
}