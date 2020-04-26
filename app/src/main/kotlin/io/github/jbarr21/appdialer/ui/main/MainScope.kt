package io.github.jbarr21.appdialer.ui.main

import io.github.jbarr21.appdialer.data.AppRepo
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.ui.ActivityLauncher
import io.github.jbarr21.appdialer.ui.main.apps.*
import io.github.jbarr21.appdialer.ui.main.dialer.*
import motif.Scope

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
  abstract class Objects : AppObjects, DialerObjects {

    fun modalFragmentListener(activityLauncher: ActivityLauncher, userCache: UserCache): ModalFragmentListener {
      return ModalFragmentListener(activityLauncher, userCache)
    }
  }
}
