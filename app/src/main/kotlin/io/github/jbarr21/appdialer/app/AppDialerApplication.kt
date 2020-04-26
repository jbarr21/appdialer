package io.github.jbarr21.appdialer.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import io.github.jbarr21.appdialer.BuildConfig
import io.github.jbarr21.appdialer.service.KeepAliveService
import io.github.jbarr21.appdialer.util.Channels
import timber.log.Timber

class AppDialerApplication : Application(), AppScopeImpl.Dependencies {

  private val appScope: AppScope by lazy { AppScopeImpl(this) }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Timber.tag("JIM").d("Application created")
    setupFlipper(this)
    createNotificationChannel()
    KeepAliveService.start(this)
  }

  override fun onTerminate() {
    Timber.tag("JIM").d("Application destroyed")
    super.onTerminate()
  }

  override fun application(): Application = this

  private fun setupFlipper(application: Application) {
    if (BuildConfig.DEBUG) {
      SoLoader.init(application, false)
      if (FlipperUtils.shouldEnableFlipper(application)) {
        AndroidFlipperClient.getInstance(application).apply {
          listOf(
            CrashReporterPlugin.getInstance(),
            DatabasesFlipperPlugin(application),
            InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()),
            NetworkFlipperPlugin(),
            SharedPreferencesFlipperPlugin(application)
          ).forEach { addPlugin(it) }
          start()
        }
      }
    }
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

  companion object {
    fun component(context: Context) = (context.applicationContext as AppDialerApplication).appScope
  }
}
