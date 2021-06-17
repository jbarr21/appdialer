package io.github.jbarr21.appdialer.util

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.LauncherApps
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import io.github.jbarr21.appdialer.data.App

class ActivityLauncher constructor(
  private val application: Application,
  private val launcherApps: LauncherApps
) {

  fun startMainActivity(app: App) {
    launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
  }

  fun uninstallApp(app: App) {
    val packageURI = Uri.parse("package:${app.packageName}")
    val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI)
    startActivity(intent, app)
  }

  fun startAppDetails(app: App) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:${app.packageName}")
    startActivity(intent, app)
  }

  fun startPlayStore(app: App) {
    val intent = try {
      Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${app.packageName}"))
    } catch (anfe: ActivityNotFoundException) {
      Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${app.packageName}"))
    }
    startActivity(intent, app)
  }

  private fun startActivity(intent: Intent, app: App) {
    intent.putExtra(Intent.EXTRA_USER, app.user)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    application.startActivity(intent)
  }
}
