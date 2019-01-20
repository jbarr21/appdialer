package io.github.jbarr21.appdialer.data

import android.content.pm.LauncherApps
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.UserManager
import androidx.palette.graphics.Palette
import com.squareup.picasso.Picasso
import io.github.jbarr21.appdialer.data.db.AppDatabase
import io.github.jbarr21.appdialer.data.db.AppEntity
import io.github.jbarr21.appdialer.util.AppIconDecoder.Companion.PARAM_USER_ID
import io.github.jbarr21.appdialer.util.AppIconDecoder.Companion.SCHEME_PNAME
import io.github.jbarr21.appdialer.ui.apps.AppAdapter
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AppRepo(
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

  private fun loadAppsFromPackageManager(): List<App> {
    return userManager.userProfiles
      .flatMap { user ->
        launcherApps.getActivityList(null, user)
          .map {
            val packageName = it.applicationInfo.packageName
            val iconUri = Uri.parse("$SCHEME_PNAME://$packageName?$PARAM_USER_ID=${user.id}")
            val icon = when (AppAdapter.imageLoader) {
              AppAdapter.ImageLoader.PICASSO -> Picasso.get().load(iconUri).get()
              AppAdapter.ImageLoader.GLIDE -> Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) //GlideApp.with(context).load(iconUri).submit().get().toBitmap()
            }
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
