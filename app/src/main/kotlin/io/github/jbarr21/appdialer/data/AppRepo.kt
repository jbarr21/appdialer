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
import coil.request.GetRequest
import io.github.jbarr21.appdialer.app.AppDialerApplication
import io.github.jbarr21.appdialer.data.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.IllegalStateException
import kotlin.coroutines.CoroutineContext

class AppRepo @Inject constructor(
  private val application: Application,
  private val appDatabase: AppDatabase,
  private val appStream: AppStream,
  private val launcherApps: LauncherApps,
  private val userCache: UserCache,
  private val userManager: UserManager
) {

  fun loadApps(useCache: Boolean = true, onComplete: () -> (Unit) = {}): Job {
    return CoroutineScope(Dispatchers.Main).launch {
      withContext(Dispatchers.IO) {
        if (useCache) {
          val cachedApps = loadAppsFromCache()
          if (cachedApps.isNotEmpty()) {
            appStream.setApps(cachedApps, this)
            return@withContext
          }
        }
        appStream.setApps(loadAppsFromPackageManager(), this)
      }
      onComplete()
    }
  }

  private suspend fun loadAppsFromCache(): List<App> {
    val memoryCachedApps = appStream.currentApps()
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
    val icon = Coil.execute(GetRequest.Builder(application).data(iconUri).build()).drawable?.toBitmap()
      ?: throw IllegalStateException("Null icon for uri: $iconUri")
    val iconColor = Palette.from(icon).generate().getDominantColor(Color.TRANSPARENT)
    return App(it.label.toString(), packageName, it.componentName.className, user, iconColor)
  }
}
