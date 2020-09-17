package io.github.jbarr21.appdialer.util

import android.app.ActivityManager
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.UserHandle
import coil.bitmappool.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.size.Size
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.isMain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconFetcher @Inject constructor(
  private val activityManager: ActivityManager,
  private val launcherApps: LauncherApps,
  private val packageManager: PackageManager,
  private val userCache: UserCache
) : Fetcher<Uri> {

  private val dpi by lazy { activityManager.launcherLargeIconDensity }
  private val defaultAppIcon by lazy {
    Resources.getSystem().getDrawableForDensity(android.R.mipmap.sym_def_app_icon, dpi)
  }

  override suspend fun fetch(pool: BitmapPool, data: Uri, size: Size, options: Options): FetchResult {
    val packageName = data.host.orEmpty()
    val user = userCache.findById(data.getQueryParameter(PARAM_USER_ID).orEmpty().toInt())
    val activityName = data.getQueryParameter(PARAM_ACTIVITY_NAME).orEmpty()
    val icon = getFullResIcon(packageName, activityName, user)
    return DrawableResult(
      drawable = icon,
      isSampled = false,
      dataSource = DataSource.DISK
    )
  }

  override fun key(data: Uri) = data.toString()

  override fun handles(data: Uri) = data.scheme == SCHEME_PNAME

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