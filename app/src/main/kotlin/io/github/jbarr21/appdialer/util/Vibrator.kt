package io.github.jbarr21.appdialer.util

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import io.github.jbarr21.appdialer.data.UserPreferencesRepo

suspend fun Vibrator?.vibrateIfAble(userPreferencesRepo: UserPreferencesRepo) {
  if (this?.hasVibrator() == true && userPreferencesRepo.useHapticFeedback()) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    } else {
      vibrate(500)
    }
  }
}