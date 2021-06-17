package io.github.jbarr21.appdialer.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService
import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import io.github.jbarr21.appdialer.service.KeepAliveService
import io.github.jbarr21.appdialer.util.Channels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltAndroidApp
class AppDialerApplication : Application(), CoroutineScope {

  @Inject lateinit var imageLoader: ImageLoader
  @Inject lateinit var userPreferencesRepo: UserPreferencesRepo

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Timber.tag("JIM").d("Application created")
    Flipper.init(this)
    createNotificationChannel()
    Coil.setImageLoader(imageLoader)
    GlobalScope.launch(Dispatchers.IO) {
      KeepAliveService.start(this@AppDialerApplication, userPreferencesRepo)
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