package io.github.jbarr21.appdialer.ui.settings

import android.content.Context
import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.helpers.*
import io.github.jbarr21.appdialer.R

object Settings {

  fun createScreen(context: Context): PreferenceScreen {
    return screen(context) {
      categoryHeader(Keys.GENERAL.name) {
        title = "General"
      }
      switch(Keys.VIBRATE.name) {
        iconRes = R.drawable.ic_vibration
        title = "Vibrate"
        summary = "Use haptic feedback on dialer key taps"
      }
      switch(Keys.SERVICE_ENABLED.name) {
        iconRes = R.drawable.ic_notifications
        title = "Persistent service"
        summary = "Use a persistent service to keep the app alive"
      }
    }
  }

  enum class Keys {
    GENERAL, VIBRATE, SERVICE_ENABLED
  }
}
