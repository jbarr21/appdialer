package io.github.jbarr21.appdialer.util

import android.content.Intent
import android.os.UserHandle
import io.github.jbarr21.appdialer.data.App

interface ActivityLauncher {
  fun startActivity(intent: Intent)

  fun startActivityInNewTask(intent: Intent)

  fun startMainActivity(app: App)

  fun uninstallApp(packageName: String, user: UserHandle)

  fun startAppDetails(packageName: String, user: UserHandle)

  fun startPlayStore(packageName: String, user: UserHandle)
}
