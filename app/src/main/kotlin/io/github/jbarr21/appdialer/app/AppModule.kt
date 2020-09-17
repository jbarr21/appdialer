package io.github.jbarr21.appdialer.app

import android.app.ActivityManager
import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.pm.LauncherApps
import android.os.UserManager
import android.os.Vibrator
import androidx.core.content.getSystemService
import androidx.room.Room
import coil.Coil
import coil.ImageLoader
import com.google.common.base.Optional
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.util.AppIconFetcher
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

  @Singleton
  @Provides
  fun activityManager(application: Application) = application.getSystemService<ActivityManager>()!!

  @Singleton
  @Provides
  fun appDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "apps").build()
  }

  fun devicePolicyManager(application: Application) = application.getSystemService<DevicePolicyManager>()!!

  @Singleton
  @Provides
  fun imageLoader(application: Application, appIconFetcher: AppIconFetcher): ImageLoader {
    return ImageLoader.Builder(application).componentRegistry {
      add(appIconFetcher)
    }.build().apply {
      Coil.setImageLoader(this)
    }
  }

  @Singleton
  @Provides
  fun launcherApps(application: Application) = application.getSystemService<LauncherApps>()!!

  @Singleton
  @Provides
  fun packageManager(application: Application) = application.packageManager

  @Singleton
  @Provides
  fun userManager(application: Application) = application.getSystemService<UserManager>()!!

  @Singleton
  @Provides
  fun vibrator(application: Application): Optional<Vibrator> = Optional.fromNullable(application.getSystemService<Vibrator>())
}