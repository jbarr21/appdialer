package io.github.jbarr21.appdialer.ui

import android.app.Application
import android.content.pm.LauncherApps
import android.os.Bundle
import android.os.Vibrator
import androidx.recyclerview.widget.DiffUtil
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.google.common.base.Optional
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.ui.apps.AppAdapter
import io.github.jbarr21.appdialer.ui.apps.AppClickStream
import io.github.jbarr21.appdialer.ui.apps.AppDiffCallback
import io.github.jbarr21.appdialer.ui.apps.ModalFragmentListener
import io.github.jbarr21.appdialer.ui.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.dialer.DialerButtonDiffCallback
import io.github.jbarr21.appdialer.util.vibrateIfAble
import motif.Scope
import java.lang.ref.WeakReference

@Scope
interface MainScope {

  fun appAdapter(): AppAdapter
  fun appClickStream(): AppClickStream
  fun appRepo(): AppRepo
  fun appStream(): AppStream
  fun dialerAdapter(): DialerAdapter
  fun dialerLabels(): List<DialerButton>
  fun keyMappings(): Map<Int, String>
  fun modalFragmentListener(): ModalFragmentListener
  fun queryStream(): QueryStream

  @motif.Objects
  abstract class Objects {

    abstract fun mainActivity(): MainActivity

    abstract fun appAdapter(): AppAdapter

    abstract fun appClickStream(): AppClickStream

    fun appDiffCallback(): DiffUtil.ItemCallback<App> = AppDiffCallback()

    abstract fun appRepo(): AppRepo

    fun dialerAdapter(
      application: Application,
      appStream: AppStream,
      queryStream: QueryStream,
      vibrator: Optional<Vibrator>) = DialerAdapter(DialerButtonDiffCallback()) { button ->
      if (appStream.currentApps().isNotEmpty()) {
        vibrator.orNull()?.vibrateIfAble(application)
        if (button.isClearButton) {
          queryStream.setQuery(emptyList())
        } else {
          queryStream.setQuery(queryStream.currentQuery() + button)
        }
      }
    }

    fun dialerLabels(): List<DialerButton> {
      return listOf(DialerButton(label = "CLEAR")) +
        keyMappings().map { (digit, letters) ->
          DialerButton(
            digit = digit,
            letters = letters
          )
        }.toList()
    }

    fun keyMappings() = mapOf(2 to "abc", 3 to "def", 4 to "ghi", 5 to "jkl", 6 to "mno", 7 to "pqrs", 8 to "tuv", 9 to "wxyz")

    fun modalFragmentListener(activityLauncher: ActivityLauncher, userCache: UserCache): ModalFragmentListener {
      return ModalFragmentListener(activityLauncher, userCache)
    }

    abstract fun queryStream(): QueryStream
  }
}
