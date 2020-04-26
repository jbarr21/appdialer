package io.github.jbarr21.appdialer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.data.AppStream
import timber.log.Timber

class PackageAddedOrRemovedReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent?) {
    Timber.tag("JIM").d("Broadcoast received!")
    Toast.makeText(context, "Broadcast received!", Toast.LENGTH_SHORT).show()
    if (intent?.action.orEmpty() in setOf(Intent.ACTION_PACKAGE_ADDED, Intent.ACTION_PACKAGE_REMOVED)) {
      refreshApps(context)
    }
  }

  private fun refreshApps(context: Context) {
    Timber.tag("JIM").d("App installed or uninstalled")
    Toast.makeText(context, "App installed or uninstalled", Toast.LENGTH_SHORT).show()
    AppDialerApplication.component(context).appStream().setApps(emptyList())
  }

  interface Parent {
    fun appStream(): AppStream
  }

  companion object {
    fun register(context: Context, receiver: PackageAddedOrRemovedReceiver) {
      val filter = IntentFilter().apply {
        setOf(Intent.ACTION_PACKAGE_ADDED, Intent.ACTION_PACKAGE_REMOVED).forEach { addAction(it) }
      }
      context.registerReceiver(receiver, filter)
      Timber.tag("JIM").d("Receiver registered")
    }

    fun unregister(context: Context, receiver: PackageAddedOrRemovedReceiver) {
      context.unregisterReceiver(receiver)
      Timber.tag("JIM").d("Receiver unregistered")
    }
  }
}
