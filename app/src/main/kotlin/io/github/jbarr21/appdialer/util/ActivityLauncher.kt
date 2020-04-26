package io.github.jbarr21.appdialer.util

import android.content.Intent

interface ActivityLauncher {
  fun startActivity(intent: Intent)
  fun startActivityInNewTask(intent: Intent)
}
