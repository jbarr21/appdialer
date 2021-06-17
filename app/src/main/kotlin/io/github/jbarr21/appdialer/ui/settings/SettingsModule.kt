package io.github.jbarr21.appdialer.ui.settings

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.SimpleListItem

@InstallIn(ViewModelComponent::class)
@Module
object SettingsModule {

  @Provides
  fun settingsData(): List<SimpleListItem<Unit>> = listOf(
    SimpleListItem<Unit>(
      label = "Vibrate",
      description = "Use haptic feedback on dialer key taps",
      iconRes = R.drawable.ic_vibration
    ),
    SimpleListItem<Unit>(
      label = "Persistent service",
      description = "Use a persistent service to keep the app alive",
      iconRes = R.drawable.ic_notifications
    )
  )
}
