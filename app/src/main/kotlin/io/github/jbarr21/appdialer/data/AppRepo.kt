package io.github.jbarr21.appdialer.data

import android.app.Application
import android.content.pm.LauncherApps
import android.graphics.Color
import android.net.Uri
import android.os.UserManager
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.GetRequest
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.PARAM_USER_ID
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.SCHEME_PNAME
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.IllegalStateException

class AppRepo @Inject constructor(
  private val application: Application,
  private val appDatabase: AppDatabase,
  private val appStream: AppStream,
  private val launcherApps: LauncherApps,
  private val userCache: UserCache,
  private val userManager: UserManager
) {
  fun loadApps(useCache: Boolean = true): Job {
    return CoroutineScope(Dispatchers.Main).launch {
      withContext(Dispatchers.IO) {
        if (useCache) {
          val cachedApps = loadAppsFromCache()
          if (cachedApps.isNotEmpty()) {
            appStream.setApps(cachedApps)
            return@withContext
          }
        }
        appStream.setApps(loadAppsFromPackageManager())
      }
    }
  }

  private fun loadAppsFromCache(): List<App> {
    val memoryCachedApps = appStream.currentApps()
    Timber.tag("JIM").d("# of memory cached apps = ${memoryCachedApps.size}")
    if (memoryCachedApps.isNotEmpty())
      return memoryCachedApps

    val diskCachedApps = appDatabase.appDao()
      .getAll()
      .map { it.toApp(userCache) }
      .toList()
    Timber.tag("JIM").d("# of disk cached apps = ${diskCachedApps.size}")
    return diskCachedApps
  }

  private suspend fun loadAppsFromPackageManager(): List<App> {
    return userManager.userProfiles
      .flatMap { user ->
        launcherApps.getActivityList(null, user)
          .map {
            val packageName = it.applicationInfo.packageName
            val iconUri = Uri.parse("$SCHEME_PNAME://$packageName?$PARAM_USER_ID=${user.id}")
            val icon = Coil.execute(GetRequest.Builder(application).data(iconUri).build()).drawable?.toBitmap()!!
            val iconColor = Palette.from(icon).generate().getDominantColor(Color.TRANSPARENT)
            App(it.label.toString(), packageName, it.componentName.className, user, iconColor)
          }
      }
      .sortedBy { app -> app.name.toLowerCase() }
      .also { apps ->
        Completable.create { emitter ->
          appDatabase.appDao().deleteAll()
          appDatabase.appDao().insertAll(*apps.map { it.toAppEntity() }.toTypedArray())
          emitter.onComplete()
        }
        .subscribeOn(Schedulers.io())
        .subscribe()
      }
  }
}
