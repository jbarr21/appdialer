package io.github.jbarr21.appdialer.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
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
import com.facebook.stetho.Stetho
import com.squareup.picasso.Picasso
import io.github.jbarr21.appdialer.app.AppScopeImpl
import io.github.jbarr21.appdialer.BuildConfig
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.service.KeepAliveService
import io.github.jbarr21.appdialer.util.AppIconRequestHandler
import io.github.jbarr21.appdialer.util.Channels
import timber.log.Timber

class AppDialerApplication : android.app.Application(), AppScopeImpl.Dependencies {

  val appScope: AppScope by lazy { AppScopeImpl(this) }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Timber.tag("JIM").d("Application created")
    // setupFlipper(this)
    Stetho.initializeWithDefaults(this)
    createNotificationChannel()
    startKeepAliveService()
    Picasso.setSingletonInstance(
      Picasso.Builder(this)
        .addRequestHandler(appScope.appIconRequestHandler())
        .build())
  }

  override fun onTerminate() {
    super.onTerminate()
    Timber.tag("JIM").d("Application destroyed")
  }

  private fun setupFlipper(application: Application) {
    if (BuildConfig.DEBUG) {
      SoLoader.init(this, false)
      if (FlipperUtils.shouldEnableFlipper(application)) {
        AndroidFlipperClient.getInstance(application).apply {
          listOf(
            InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()),
            SharedPreferencesFlipperPlugin(application),
            NetworkFlipperPlugin(),
            DatabasesFlipperPlugin(application),
            CrashReporterPlugin.getInstance()
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

  private fun startKeepAliveService() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForegroundService(Intent(this, KeepAliveService::class.java))
    }
  }

  override fun application(): Application = this

  companion object {
    fun component(context: Context) = (context.applicationContext as AppDialerApplication).appScope
  }
}
