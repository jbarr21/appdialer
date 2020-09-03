package io.github.jbarr21.appdialer.ui.main

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Vibrator
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import com.google.common.base.Optional
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
import io.github.jbarr21.appdialer.ui.main.dialer.DialerViewModel
import io.github.jbarr21.appdialer.ui.main.dialer.QueryStream
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.vibrateIfAble

@InstallIn(ActivityComponent::class)
@Module
object MainModule {

  @Provides
  fun activityLauncher(activity: FragmentActivity): ActivityLauncher {
    return object : ActivityLauncher {
      override fun startActivity(intent: Intent) = activity.startActivity(intent)
      override fun startActivityInNewTask(intent: Intent) {
        activity.startActivity(intent.run { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
      }
    }
  }

  @Provides
  fun dialerViewModel(activity: FragmentActivity) = ViewModelProviders.of(activity).get(DialerViewModel::class.java)

  @Provides
  fun modalFragmentListener(activityLauncher: ActivityLauncher, userCache: UserCache) = ModalFragmentListener(activityLauncher, userCache)

  @Provides
  fun fragmentManager(activity: FragmentActivity) = activity.supportFragmentManager

  @Provides
  fun appDiffCallback(callback: AppDiffCallback): DiffUtil.ItemCallback<App> = callback

  // Dialer

  @Provides
  fun dialerAdapter(
    activity: Activity,
    application: Application,
    appStream: AppStream,
    dialerLabels: List<DialerButton>,
    queryStream: QueryStream,
    vibrator: Optional<Vibrator>
  ) = DialerAdapter(
    DialerButtonDiffCallback(),
    dialerLabels,
    { button ->
      if (button.isClearButton || button.isInfoButton) {
        vibrator.orNull()?.vibrateIfAble(application)
        queryStream.longClick(button)
      }
    },
    { button ->
      if (appStream.currentApps().isNotEmpty()) {
        vibrator.orNull()?.vibrateIfAble(application)
        if (button.isClearButton) {
          queryStream.setQuery(emptyList())
        } else {
          queryStream.setQuery(queryStream.currentQuery() + button)
        }
      }
    }
  )

  @Provides
  fun dialerLabels(): List<DialerButton> {
    return listOf(DialerButton(label = "CLEAR *")) +
      keyMappings().map { (digit, letters) ->
        DialerButton(
          digit = digit,
          letters = letters
        )
      }.toList()
  }

  @Provides
  fun keyMappings() = mapOf(2 to "abc", 3 to "def", 4 to "ghi", 5 to "jkl", 6 to "mno", 7 to "pqrs", 8 to "tuv", 9 to "wxyz")
}
