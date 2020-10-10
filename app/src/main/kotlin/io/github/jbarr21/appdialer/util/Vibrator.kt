package io.github.jbarr21.appdialer.util

import android.app.Application
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.getSystemService
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class Vibrator @Inject constructor(
  private val application: Application,
  private val userPreferencesRepo: UserPreferencesRepo
) {

  private val vibrator by lazy { application.getSystemService<Vibrator>() }

  fun vibrate() {
    GlobalScope.launch {
      if (vibrator?.hasVibrator() == true && userPreferencesRepo.useHapticFeedback()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
          @Suppress("DEPRECATION")
          vibrator?.vibrate(500)
        }
      }
    }
  }
}
