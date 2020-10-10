package io.github.jbarr21.appdialer.util

import android.content.Intent
import io.github.jbarr21.appdialer.data.App

interface ActivityLauncher {
  fun startActivity(intent: Intent)

  fun startActivityInNewTask(intent: Intent)

  fun startMainActivity(app: App)
}
