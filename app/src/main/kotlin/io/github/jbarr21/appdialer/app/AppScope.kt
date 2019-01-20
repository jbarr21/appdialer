package io.github.jbarr21.appdialer.app

import android.app.ActivityManager
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.pm.LauncherApps
import android.os.UserManager
import androidx.core.content.getSystemService
import androidx.room.Room
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.db.AppDatabase
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
  fun userCache(): UserCache
  fun userManager(): UserManager

  @motif.Objects
  abstract class Objects {

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
    abstract fun userCache(): UserCache

    @Expose
    fun userManager(application: Application) = application.getSystemService<UserManager>()!!
  }
}
