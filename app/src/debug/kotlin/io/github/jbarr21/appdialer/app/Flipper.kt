package io.github.jbarr21.appdialer.app

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader

object Flipper {

  internal fun init(application: Application) {
    SoLoader.init(application, false)
    if (FlipperUtils.shouldEnableFlipper(application)) {
      AndroidFlipperClient.getInstance(application).apply {
        listOf(
          CrashReporterPlugin.getInstance(),
          DatabasesFlipperPlugin(application),
          InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()),
          NavigationFlipperPlugin.getInstance(),
          NetworkFlipperPlugin(),
          SharedPreferencesFlipperPlugin(application)
        ).forEach { addPlugin(it) }
        start()
      }
    }
  }
}
