package io.github.jbarr21.appdialer.ui

import android.content.Intent

interface ActivityLauncher {
  fun startActivity(intent: Intent)
}
