package io.github.jbarr21.appdialer.ui.main

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.ui.main.apps.*
import io.github.jbarr21.appdialer.ui.main.dialer.*
import motif.Scope

@Scope
interface MainScope {

  fun coordinator(): MainCoordinator

  @motif.Objects
  abstract class Objects : AppObjects, DialerObjects {

    abstract fun activity(mainActivity: MainActivity): Activity

    fun activityLauncher(activity: Activity): ActivityLauncher {
      return object : ActivityLauncher {
        override fun startActivity(intent: Intent) = activity.startActivity(intent)
        override fun startActivityInNewTask(intent: Intent) {
          activity.startActivity(intent.run { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        }
      }
    }

    fun dialerViewModel(mainActivity: MainActivity) = ViewModelProviders.of(mainActivity).get(DialerViewModel::class.java)

    abstract fun mainCoordinator(): MainCoordinator

    fun modalFragmentListener(activityLauncher: ActivityLauncher, userCache: UserCache) = ModalFragmentListener(activityLauncher, userCache)

    fun fragmentManager(activity: MainActivity) = activity.supportFragmentManager
  }
}
