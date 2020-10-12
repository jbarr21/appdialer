package io.github.jbarr21.appdialer.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.LauncherApps
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.scopes.ActivityScoped
import io.github.jbarr21.appdialer.data.App
import javax.inject.Inject

@ActivityScoped
class ActivityLauncher @Inject constructor(
  private val activity: FragmentActivity,
  private val launcherApps: LauncherApps
) {

  fun startActivity(intent: Intent) = activity.startActivity(intent)

  fun startActivityInNewTask(intent: Intent) {
    activity.startActivity(intent.run { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
  }

  fun startMainActivity(app: App) {
    launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
  }

  fun uninstallApp(app: App) {
    val packageURI = Uri.parse("package:${app.packageName}")
    val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI)
    intent.putExtra(Intent.EXTRA_USER, app.user)
    startActivity(intent)
  }

  fun startAppDetails(app: App) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:${app.packageName}")
    intent.putExtra(Intent.EXTRA_USER, app.user)
    startActivity(intent)
  }

  fun startPlayStore(app: App) {
    val intent = try {
      Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${app.packageName}"))
    } catch (anfe: ActivityNotFoundException) {
      Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${app.packageName}"))
    }
    intent.putExtra(Intent.EXTRA_USER, app.user)
    startActivity(intent)
  }
}
