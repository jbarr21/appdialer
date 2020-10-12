package io.github.jbarr21.appdialer.data

import android.app.Application
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.graphics.Color
import android.os.UserHandle
import android.os.UserManager
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import io.github.jbarr21.appdialer.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AppRepo @Inject constructor(
  private val application: Application,
  private val appDatabase: AppDatabase,
  private val launcherApps: LauncherApps,
  private val userCache: UserCache,
  private val userManager: UserManager
) {

  val cachedApps: MutableList<App> = mutableListOf()

  suspend fun loadApps(useCache: Boolean): List<App> {
    return withContext(Dispatchers.IO) {
      if (useCache) {
        val cachedApps = loadAppsFromCache()
        if (cachedApps.isNotEmpty()) return@withContext cachedApps
      }
      val pmApps = loadAppsFromPackageManager()
      return@withContext pmApps
    }
  }

  private suspend fun loadAppsFromCache(): List<App> {
    val memoryCachedApps = cachedApps
    Timber.tag("JIM").d("# of memory cached apps = ${memoryCachedApps.size}")
    return if (memoryCachedApps.isNotEmpty())
      memoryCachedApps
    else
      appDatabase.appDao()
        .getAll()
        .map { it.toApp(userCache) }
        .toList()
        .also {
          Timber.tag("JIM").d("# of disk cached apps = ${it.size}")
          cachedApps.addAll(it)
        }
  }

  private suspend fun loadAppsFromPackageManager(): List<App> = coroutineScope {
    Timber.tag("JIM").d("start load apps from pkg mgr")
    userManager.userProfiles
      .map { user ->
        async { loadAppsFromPackageManagerForUser(user) }
      }
      .awaitAll()
      .flatten()
      .sortedBy { app -> app.name.toLowerCase() }
      .also { apps ->
        appDatabase.appDao().deleteAll()
        appDatabase.appDao().insertAll(*apps.map { it.toAppEntity() }.toTypedArray())
        Timber.tag("JIM").d("finish load apps from pkg mgr")
      }
  }

  private suspend fun loadAppsFromPackageManagerForUser(user: UserHandle): List<App> = coroutineScope {
    launcherApps.getActivityList(null, user)
      .map {
        async { createApp(it, user) }
      }
      .awaitAll()
  }

  private suspend fun createApp(it: LauncherActivityInfo, user: UserHandle): App {
    val packageName = it.applicationInfo.packageName
    val activityName: String = it.componentName.className
    val iconUri = App.iconUri(packageName, activityName, user)
    val icon = Coil.execute(ImageRequest.Builder(application).data(iconUri).build()).drawable?.toBitmap()
      ?: throw IllegalStateException("Null icon for uri: $iconUri")
    val iconColor = Palette.from(icon).generate().getDominantColor(Color.TRANSPARENT)
    return App(it.label.toString(), packageName, it.componentName.className, user, iconColor)
  }
}