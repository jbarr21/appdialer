package io.github.jbarr21.appdialer.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.ui.MainActivity
import io.github.jbarr21.appdialer.util.Channels
import timber.log.Timber

class KeepAliveService : Service() {

  val appReceiver by lazy { PackageAddedOrRemovedReceiver() }

  override fun onCreate() {
    Timber.tag("JIM").d("Service created")
    startForeground(1337, createNotification())
    PackageAddedOrRemovedReceiver.register(this, appReceiver)
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
    PackageAddedOrRemovedReceiver.unregister(this, appReceiver)
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
}
