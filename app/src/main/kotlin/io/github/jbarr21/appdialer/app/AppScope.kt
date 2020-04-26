package io.github.jbarr21.appdialer.app

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.LauncherApps
import android.os.UserManager
import android.os.Vibrator
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import androidx.room.Room
import coil.Coil
import coil.ImageLoader
import com.google.common.base.Optional
import io.github.jbarr21.appdialer.data.AppStream
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.service.KeepAliveService
import io.github.jbarr21.appdialer.service.PackageAddedOrRemovedReceiver
import io.github.jbarr21.appdialer.ui.main.MainActivity
import io.github.jbarr21.appdialer.ui.main.MainScope
import io.github.jbarr21.appdialer.util.AppIconFetcher
import motif.Expose
import motif.Scope

@Scope
interface AppScope
  : KeepAliveService.Parent,
  PackageAddedOrRemovedReceiver.Parent {

  fun mainScope(mainActivity: MainActivity): MainScope

  @motif.Objects
  abstract class Objects {

    fun activityManager(application: Application) = application.getSystemService<ActivityManager>()!!

    @Expose
    fun appDatabase(application: Application): AppDatabase {
      return Room.databaseBuilder(application, AppDatabase::class.java, "apps").build()
    }

    abstract fun appIconFetcher(): AppIconFetcher

    @Expose
    fun appStream() = AppStream()

    fun devicePolicyManager(application: Application) = application.getSystemService<DevicePolicyManager>()!!

    @Expose
    fun imageLoader(application: Application, appIconFetcher: AppIconFetcher): ImageLoader {
      return ImageLoader.Builder(application).componentRegistry {
        add(appIconFetcher)
      }.build().apply {
        Coil.setImageLoader(this)
      }
    }

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
