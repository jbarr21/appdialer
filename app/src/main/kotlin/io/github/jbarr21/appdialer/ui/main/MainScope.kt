package io.github.jbarr21.appdialer.ui.main

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.databinding.ActivityMainBinding
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.ui.main.apps.*
import io.github.jbarr21.appdialer.ui.main.dialer.*
import motif.Scope

@Scope
interface MainScope {

  fun coordinator(): MainCoordinator

  @motif.Objects
  abstract class Objects : AppObjects, DialerObjects {

    abstract fun activity(activity: AppCompatActivity): Activity

    fun activityLauncher(activity: Activity): ActivityLauncher {
      return object : ActivityLauncher {
        override fun startActivity(intent: Intent) = activity.startActivity(intent)
        override fun startActivityInNewTask(intent: Intent) {
          activity.startActivity(intent.run { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        }
      }
    }

    fun dialerViewModel(activity: AppCompatActivity) = ViewModelProviders.of(activity).get(DialerViewModel::class.java)

    abstract fun mainCoordinator(): MainCoordinator

    fun modalFragmentListener(activityLauncher: ActivityLauncher, userCache: UserCache) = ModalFragmentListener(activityLauncher, userCache)

    fun fragmentManager(activity: AppCompatActivity) = activity.supportFragmentManager

    fun viewBinding(viewGroup: ViewGroup) = ActivityMainBinding.bind(viewGroup.getChildAt(0))
  }
}
