package io.github.jbarr21.appdialer.ui

import android.content.pm.LauncherApps
import android.os.Bundle
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.ui.apps.AppAdapter
import io.github.jbarr21.appdialer.ui.apps.AppDiffCallback
import io.github.jbarr21.appdialer.ui.apps.ModalFragmentListener
import io.github.jbarr21.appdialer.ui.dialer.DialerAdapter
import io.github.jbarr21.appdialer.ui.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.dialer.DialerButtonDiffCallback
import motif.Scope
import java.lang.ref.WeakReference

@Scope
interface MainScope {

  fun appAdapter(): AppAdapter
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

    fun appAdapter(mainActivity: MainActivity, launcherApps: LauncherApps, queryStream: QueryStream) = AppAdapter(
      AppDiffCallback(),
      queryStream,
      { app -> run {
        launcherApps.startMainActivity(app.launchIntent.component, app.user, null, Bundle.EMPTY)
        mainActivity.finish()
        queryStream.setQuery(emptyList())
      }}, { app -> run {
        // TODO: fix how to pass this (use another lib?)
        mainActivity.longPressedApp = app
        ModalBottomSheetDialogFragment.Builder()
          .header(app.label)
          .add(R.menu.app_details)
          .build()
          .show(mainActivity.supportFragmentManager, app.uri.toString())
      }})

    abstract fun appRepo(): AppRepo

    fun dialerAdapter(appStream: AppStream, queryStream: QueryStream) = DialerAdapter(DialerButtonDiffCallback()) { button ->
      if (appStream.currentApps().isNotEmpty()) {
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

    fun modalFragmentListener(mainActivity: MainActivity, userCache: UserCache): ModalFragmentListener {
      return ModalFragmentListener(WeakReference(mainActivity), userCache)
    }

    abstract fun queryStream(): QueryStream
  }
}
