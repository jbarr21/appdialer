package io.github.jbarr21.appdialer.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.LauncherApps
import android.net.Uri
import android.os.Bundle
import android.os.UserHandle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import io.github.jbarr21.appdialer.data.App

class ActivityLauncherImpl(
  private val activity: FragmentActivity,
  private val launcherApps: LauncherApps
) : ActivityLauncher {

  override fun startActivity(intent: Intent) = activity.startActivity(intent)

  override fun startActivityInNewTask(intent: Intent) {
    activity.startActivity(intent.run { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
  }

  override fun startMainActivity(app: App) {
    launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
  }

  override fun uninstallApp(packageName: String, user: UserHandle) {
    val packageURI = Uri.parse("package:$packageName")
    val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI)
    intent.putExtra(Intent.EXTRA_USER, user)
    startActivity(intent)
  }

  override fun startAppDetails(packageName: String, user: UserHandle) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    intent.putExtra(Intent.EXTRA_USER, user)
    startActivity(intent)
  }

  override fun startPlayStore(packageName: String, user: UserHandle) {
    val intent = try {
      Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    } catch (anfe: ActivityNotFoundException) {
      Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
    }
    intent.putExtra(Intent.EXTRA_USER, user)
    startActivity(intent)
  }
}
