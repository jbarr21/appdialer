package io.github.jbarr21.appdialer.service

import android.content.*
import android.widget.Toast
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.data.AppStream
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PackageAddedOrRemovedReceiver : BroadcastReceiver() {

  @Inject
  lateinit var appStream: AppStream

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
    appStream.setApps(emptyList())
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
