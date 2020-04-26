package io.github.jbarr21.appdialer.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.preference.PreferenceManager
import io.github.jbarr21.appdialer.ui.settings.Settings

fun Vibrator?.vibrateIfAble(context: Context) {
  val shouldVibrate = PreferenceManager.getDefaultSharedPreferences(context)
    .getBoolean(Settings.Keys.VIBRATE.name, false)

  if (shouldVibrate && this?.hasVibrator() == true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    } else {
      vibrate(500)
    }
  }
}
