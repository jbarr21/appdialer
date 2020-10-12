package io.github.jbarr21.appdialer.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import io.github.jbarr21.appdialer.ui.main.MainActivity
import io.github.jbarr21.appdialer.util.Channels
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class KeepAliveService : Service() {

  @Inject
  lateinit var userPreferencesRepo: UserPreferencesRepo

  override fun onCreate() {
    super.onCreate()
    Timber.tag("JIM").d("Service created")
    startForeground(1337, createNotification())
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Timber.tag("JIM").d("Service started")
    return START_STICKY
  }

  override fun onBind(intent: Intent): IBinder? {
    Timber.tag("JIM").d("Service bound")
    return null
  }

  override fun onUnbind(intent: Intent): Boolean {
    Timber.tag("JIM").d("Service unbound")
    return false
  }

  override fun onRebind(intent: Intent) {
    Timber.tag("JIM").d("Service rebound")
  }

  override fun onDestroy() {
    Timber.tag("JIM").d("Service destroyed")
  }

  private fun createNotification(): Notification {
    val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { intent ->
      PendingIntent.getActivity(this, 0, intent, 0)
    }

    val notificationBuilder = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
      Notification.Builder(this, Channels.list[0].id)
    else
      Notification.Builder(this)

    return notificationBuilder
      .setContentTitle("AppDialer")
      .setContentText("Indexing available apps...")
      .setSmallIcon(R.drawable.ic_apps_white_24dp)
      .setContentIntent(pendingIntent)
      .setTicker("Ticker text")
      .build()
  }

  companion object {
    suspend fun start(context: Context, userPreferencesRepo: UserPreferencesRepo) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (userPreferencesRepo.usePersistentService()) {
          context.startForegroundService(Intent(context, KeepAliveService::class.java))
        }
      }
    }
  }
}
