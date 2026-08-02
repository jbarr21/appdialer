package io.github.jbarr21.appdialer.util

import android.app.ActivityManager
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.UserHandle
import androidx.collection.LruCache
import coil3.ImageLoader
import coil3.Uri
import coil3.asImage
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.fetch.FetchResult
import coil3.decode.DataSource
import coil3.request.Options
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.isMain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconFetcher @Inject constructor(
  private val activityManager: ActivityManager,
  private val imageCache: LruCache<String, Drawable>,
  private val launcherApps: LauncherApps,
  private val packageManager: PackageManager,
  private val userCache: UserCache,
  private val data: Uri
) : Fetcher {

  private val dpi by lazy { activityManager.launcherLargeIconDensity }
  private val defaultAppIcon by lazy {
    Resources.getSystem().getDrawableForDensity(android.R.mipmap.sym_def_app_icon, dpi)
  }

  override suspend fun fetch(): FetchResult {
    return imageCache.get(data.toString())?.let { icon ->
      ImageFetchResult(
        image = icon.asImage(),
        isSampled = false,
        dataSource = DataSource.MEMORY
      )
    } ?: fetchUncachedIcon(data)
  }

  private fun fetchUncachedIcon(data: Uri): ImageFetchResult {
    val packageName = data.authority.orEmpty()
    val parameters = data.query.orEmpty().split("&").associate { parameter ->
      parameter.substringBefore("=") to parameter.substringAfter("=", "")
    }
    val user = userCache.findById(parameters[PARAM_USER_ID].orEmpty().toInt())
    val activityName = parameters[PARAM_ACTIVITY_NAME].orEmpty()
    val icon = getFullResIcon(packageName, activityName, user)
    imageCache.put(data.toString(), icon)
    return ImageFetchResult(
      image = icon.asImage(),
      isSampled = false,
      dataSource = DataSource.DISK
    )
  }

  class Factory @Inject constructor(
    private val activityManager: ActivityManager,
    private val imageCache: LruCache<String, Drawable>,
    private val launcherApps: LauncherApps,
    private val packageManager: PackageManager,
    private val userCache: UserCache
  ) : Fetcher.Factory<Uri> {
    override fun create(data: Uri, options: Options, imageLoader: ImageLoader): Fetcher? {
      return AppIconFetcher(activityManager, imageCache, launcherApps, packageManager, userCache, data)
        .takeIf { data.scheme == SCHEME_PNAME }
    }
  }

  private fun getFullResIcon(packageName: String, activityName: String, user: UserHandle): Drawable {
    try {
      return launcherApps.getActivityList(packageName, user).let {
        if (it.size == 1) it.first() else it.first { it.componentName.className == activityName }
      }.getIcon(dpi).let { icon ->
        if (user.isMain) icon else packageManager.getUserBadgedIcon(icon, user)
      }
    } catch (ignored: PackageManager.NameNotFoundException) { } catch (ignored: Resources.NotFoundException) { }

    return defaultAppIcon!!
  }

  companion object {
    const val SCHEME_PNAME = "pname"
    const val PARAM_ACTIVITY_NAME = "activityName"
    const val PARAM_USER_ID = "userId"
  }
}
