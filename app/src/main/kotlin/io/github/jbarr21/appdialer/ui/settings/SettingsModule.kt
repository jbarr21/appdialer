package io.github.jbarr21.appdialer.ui.settings

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.jbarr21.appdialer.R

@InstallIn(ActivityComponent::class)
@Module
object SettingsModule {

  @Provides
  fun settingsData() = listOf(
    Setting(
      title = "Vibrate",
      description = "Use haptic feedback on dialer key taps",
      iconRes = R.drawable.ic_vibration
    ),
    Setting(
      title = "Persistent service",
      description = "Use a persistent service to keep the app alive",
      iconRes = R.drawable.ic_notifications
    )
  )
}