package io.github.jbarr21.appdialer.app

import android.app.ActivityManager
import android.app.Application
import android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP
import android.content.pm.LauncherApps
import android.graphics.drawable.Drawable
import android.os.UserManager
import androidx.collection.LruCache
import androidx.core.content.getSystemService
import androidx.room.Room
import coil3.ImageLoader
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.ui.info.InfoViewModel
import io.github.jbarr21.appdialer.ui.main.MainViewModel
import io.github.jbarr21.appdialer.ui.settings.SettingsViewModel
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.AppIconFetcher

@dev.zacsweers.metro.Scope
annotation class AppScope

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
interface AppGraph {

  val imageLoader: ImageLoader
  val userPreferencesRepo: UserPreferencesRepo
  val mainViewModel: MainViewModel
  val settingsViewModel: SettingsViewModel
  val infoViewModel: InfoViewModel

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides application: Application): AppGraph
  }

  @Provides
  fun activityLauncher(application: Application, launcherApps: LauncherApps): ActivityLauncher =
    ActivityLauncher(application, launcherApps)

  @Provides
  fun activityManager(application: Application): ActivityManager =
    application.getSystemService<ActivityManager>()!!

  @Provides
  fun appDatabase(application: Application): AppDatabase =
    Room.databaseBuilder(application, AppDatabase::class.java, "apps").build()

  @Provides
  fun imageCache(am: ActivityManager, application: Application): LruCache<String, Drawable> {
    val largeHeap = application.applicationInfo.flags and FLAG_LARGE_HEAP != 0
    val memoryClass = if (largeHeap) am.largeMemoryClass else am.memoryClass
    val maxSize = (1024L * 1024L * memoryClass / 7).toInt()
    return LruCache(maxSize)
  }

  @Provides
  fun imageLoader(application: Application, appIconFetcher: AppIconFetcher.Factory): ImageLoader =
    ImageLoader.Builder(application).components { add(appIconFetcher) }.build()

  @Provides
  fun launcherApps(application: Application): LauncherApps =
    application.getSystemService<LauncherApps>()!!

  @Provides
  fun packageManager(application: Application): android.content.pm.PackageManager = application.packageManager

  @Provides
  fun userManager(application: Application): UserManager =
    application.getSystemService<UserManager>()!!

  @Provides
  @SingleIn(AppScope::class)
  fun userPreferencesRepo(application: Application): UserPreferencesRepo = UserPreferencesRepo(application)

  @Provides
  fun appLongClickActions(activityLauncher: ActivityLauncher): List<SimpleListItem<App>> = listOf(
    SimpleListItem("Uninstall", iconRes = R.drawable.ic_delete_black_24dp) {
      activityLauncher.uninstallApp(it)
    },
    SimpleListItem("App Info", iconRes = R.drawable.ic_info_black_24dp) {
      activityLauncher.startAppDetails(it)
    },
    SimpleListItem("Play Store", iconRes = R.drawable.ic_local_grocery_store_black_24dp) {
      activityLauncher.startPlayStore(it)
    }
  )

  @Provides
  fun dialerLabels(): List<DialerButton> = listOf(DialerButton(label = "CLEAR *")) +
    DialerModule.keyMappings().map { (digit, letters) ->
      DialerButton(digit = digit, letters = letters)
    }

  @Provides
  fun settingsData(): List<SimpleListItem<Unit>> = listOf(
    SimpleListItem(
      label = "Vibrate",
      description = "Use haptic feedback on dialer key taps",
      iconRes = R.drawable.ic_vibration
    ),
    SimpleListItem(
      label = "Persistent service",
      description = "Use a persistent service to keep the app alive",
      iconRes = R.drawable.ic_notifications
    )
  )
}

private object DialerModule {
  fun keyMappings(): Map<Int, String> = (2 until 10).mapIndexed { index, digit ->
    val fourSet = setOf(7, 9)
    val numLetters = if (digit in fourSet) 4 else 3
    val letters = (0 until numLetters).map {
      val offset = index * 3 + it + fourSet.count { digit > it }
      ('a' + offset)
    }
    digit to letters.joinToString(separator = "")
  }.toMap()
}
