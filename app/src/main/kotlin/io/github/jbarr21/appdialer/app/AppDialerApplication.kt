package io.github.jbarr21.appdialer.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService
import coil3.SingletonImageLoader
import io.github.jbarr21.appdialer.service.KeepAliveService
import io.github.jbarr21.appdialer.util.Channels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import dev.zacsweers.metro.createGraphFactory
import kotlin.coroutines.CoroutineContext

class AppDialerApplication : Application(), CoroutineScope {

  lateinit var appGraph: AppGraph
    private set

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main

  override fun onCreate() {
    super.onCreate()
    appGraph = createGraphFactory<AppGraph.Factory>().create(this)
    Timber.plant(Timber.DebugTree())
    Timber.tag("JIM").d("Application created")
    createNotificationChannel()
    SingletonImageLoader.setSafe { appGraph.imageLoader }
    GlobalScope.launch(Dispatchers.IO) {
      KeepAliveService.start(this@AppDialerApplication, appGraph.userPreferencesRepo)
    }
  }

  override fun onTerminate() {
    cancel()
    Timber.tag("JIM").d("Application destroyed")
    super.onTerminate()
  }

  private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Channels.list.forEach {
        NotificationChannel(it.id, it.name, it.importance).apply {
          description = "General notifications"
          getSystemService<NotificationManager>()?.createNotificationChannel(this)
        }
      }
    }
  }
}
