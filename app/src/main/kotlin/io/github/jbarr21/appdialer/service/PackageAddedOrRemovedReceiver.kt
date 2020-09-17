package io.github.jbarr21.appdialer.service

import android.content.*
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PackageAddedOrRemovedReceiver : BroadcastReceiver() {

//  @Inject
//  lateinit var appStream: AppStream

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
//    appStream.setApps(emptyList())
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
      try {
        context.unregisterReceiver(receiver)
        Timber.tag("JIM").d("Receiver unregistered")
      } catch (e: IllegalArgumentException) {
        if ("Receiver not registered" !in e.message.orEmpty()) {
          throw e
        }
      }
    }
  }
}