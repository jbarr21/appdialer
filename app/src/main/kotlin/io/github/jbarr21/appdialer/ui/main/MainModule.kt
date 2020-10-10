package io.github.jbarr21.appdialer.ui.main

import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.ui.main.apps.AppDiffCallback
import io.github.jbarr21.appdialer.ui.main.apps.ModalFragmentListener
import io.github.jbarr21.appdialer.ui.main.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButtonDiffCallback
import io.github.jbarr21.appdialer.ui.main.dialer.QueryStream
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.Vibrator
import kotlinx.coroutines.launch

@InstallIn(ActivityComponent::class)
@Module
object MainModule {

  @Provides
  fun activityLauncher(activity: FragmentActivity, launcherApps: LauncherApps): ActivityLauncher {
    return object : ActivityLauncher {
      override fun startActivity(intent: Intent) = activity.startActivity(intent)

      override fun startActivityInNewTask(intent: Intent) {
        activity.startActivity(intent.run { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
      }

      override fun startMainActivity(app: App) {
        launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
      }
    }
  }

  @Provides
  fun modalFragmentListener(activityLauncher: ActivityLauncher, userCache: UserCache) = ModalFragmentListener(activityLauncher, userCache)

  @Provides
  fun fragmentManager(activity: FragmentActivity) = activity.supportFragmentManager

  @Provides
  fun appDiffCallback(callback: AppDiffCallback): DiffUtil.ItemCallback<App> = callback

  // Dialer

  @Provides
  fun dialerAdapter(
    activity: FragmentActivity,
    appStream: AppStream,
    dialerLabels: List<DialerButton>,
    queryStream: QueryStream,
    vibrator: Vibrator
  ) = DialerAdapter(
    DialerButtonDiffCallback(),
    dialerLabels,
    { button ->
      if (button.isClearButton || button.isInfoButton) {
        activity.lifecycleScope.launch {
          vibrator.vibrate()
        }
        // TODO: switch from activity lifecycle to coordinator (x3)
        queryStream.longClick(button, activity.lifecycleScope)
      }
    },
    { button ->
      if (appStream.currentApps().isNotEmpty()) {
        activity.lifecycleScope.launch {
          vibrator.vibrate()
        }
        if (button.isClearButton) {
          queryStream.setQuery(emptyList(), activity.lifecycleScope)
        } else {
          queryStream.setQuery(queryStream.currentQuery() + button, activity.lifecycleScope)
        }
      }
    }
  )

  @Provides
  fun dialerLabels(): List<DialerButton> {
    return listOf(DialerButton(label = "CLEAR *")) + keyMappings().map { (digit, letters) ->
      DialerButton(digit = digit, letters = letters)
    }.toList()
  }

  fun keyMappings() = (2 until 10).mapIndexed { index, digit ->
    val fourSet = setOf(7, 9)
    val numLetters = if (digit in fourSet) 4 else 3
    val letters = (0 until numLetters).map {
      val offset = index * 3 + it + fourSet.count { digit > it }
      return@map ('a' + offset)
    }
    return@mapIndexed digit to letters.joinToString(separator = "")
  }.toMap()
}
