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
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jbarr21.appdialer.data.UserPreferencesRepo
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.AppIconFetcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  @Provides
  fun activityLauncher(
    application: Application,
    launcherApps: LauncherApps
  ): ActivityLauncher = ActivityLauncher(application, launcherApps)

  @Provides
  fun activityManager(application: Application) = application.getSystemService<ActivityManager>()!!

  @Provides
  fun appDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "apps").build()
  }

  @Provides
  fun imageCache(am: ActivityManager, application: Application): LruCache<String, Drawable> {
    val largeHeap = application.applicationInfo.flags and FLAG_LARGE_HEAP !== 0
    val memoryClass = if (largeHeap) am.largeMemoryClass else am.memoryClass
    // Target ~15% of the available heap.
    val maxSize = (1024L * 1024L * memoryClass / 7).toInt()
    return LruCache<String, Drawable>(maxSize)
  }

  @Provides
  fun imageLoader(application: Application, appIconFetcher: AppIconFetcher): ImageLoader {
    return ImageLoader.Builder(application).componentRegistry {
      add(appIconFetcher)
    }.build()
  }

  @Provides
  fun launcherApps(application: Application) = application.getSystemService<LauncherApps>()!!

  @Provides
  fun packageManager(application: Application) = application.packageManager

  @Provides
  fun userManager(application: Application) = application.getSystemService<UserManager>()!!

  @Singleton
  @Provides
  fun userPreferencesRepo(application: Application) = UserPreferencesRepo(application)
}