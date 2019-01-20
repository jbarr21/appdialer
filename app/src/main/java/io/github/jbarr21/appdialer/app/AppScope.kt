package io.github.jbarr21.appdialer.app

import android.app.ActivityManager
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.LauncherApps
import android.os.UserManager
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import com.google.common.base.Optional
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.ui.ActivityLauncher
import io.github.jbarr21.appdialer.ui.MainActivity
import io.github.jbarr21.appdialer.ui.MainScope
import io.github.jbarr21.appdialer.ui.MainScopeImpl
import io.github.jbarr21.appdialer.util.AppIconDecoder
import io.github.jbarr21.appdialer.util.AppIconRequestHandler
import motif.Expose
import motif.Scope

@Scope
interface AppScope {

  fun mainScope(mainActivity: MainActivity): MainScope

  fun appDatabase(): AppDatabase
  fun appIconDecoder(): AppIconDecoder
  fun appIconRequestHandler(): AppIconRequestHandler
  fun appStream(): AppStream
  fun launcherApps(): LauncherApps
  fun sharedPreferences(): SharedPreferences
  fun userCache(): UserCache
  fun userManager(): UserManager

  @motif.Objects
  abstract class Objects {

    @Expose
    fun activityLauncher(application: Application): ActivityLauncher {
      return object : ActivityLauncher {
        override fun startActivity(intent: Intent) {
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          application.startActivity(intent)
        }
      }
    }

    fun activityManager(application: Application) = application.getSystemService<ActivityManager>()!!

    @Expose
    fun appDatabase(application: Application): AppDatabase {
      return Room.databaseBuilder(application, AppDatabase::class.java, "apps").build()
    }

    abstract fun appIconDecoder(): AppIconDecoder

    abstract fun appIconRequestHandler(): AppIconRequestHandler

    @Expose
    fun appStream() = AppStream()

    fun devicePolicyManager(application: Application) = application.getSystemService<DevicePolicyManager>()!!

    @Expose
    fun launcherApps(application: Application) = application.getSystemService<LauncherApps>()!!

    fun packageManager(application: Application) = application.packageManager

    @Expose
    fun sharedPreferences(application: Application): SharedPreferences {
      return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Expose
    abstract fun userCache(): UserCache

    @Expose
    fun userManager(application: Application) = application.getSystemService<UserManager>()!!

    @Expose
    fun vibrator(application: Application): Optional<Vibrator> = Optional.fromNullable(application.getSystemService<Vibrator>())
  }
}
