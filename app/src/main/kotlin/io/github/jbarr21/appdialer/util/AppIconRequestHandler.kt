package io.github.jbarr21.appdialer.util

import android.app.ActivityManager
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.UserHandle
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import com.squareup.picasso.RequestHandler
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.isMain
import io.github.jbarr21.appdialer.util.AppIconDecoder.Companion.PARAM_USER_ID
import io.github.jbarr21.appdialer.util.AppIconDecoder.Companion.SCHEME_PNAME

@Deprecated("Switch to using Glide")
class AppIconRequestHandler(
  private val activityManager: ActivityManager,
  private val launcherApps: LauncherApps,
  private val packageManager: PackageManager,
  private val userCache: UserCache
) : RequestHandler() {

  private val dpi by lazy { activityManager.launcherLargeIconDensity }
  private val defaultAppIcon by lazy {
    Resources.getSystem().getDrawableForDensity(android.R.mipmap.sym_def_app_icon, dpi)!!.toBitmap()
  }

  override fun canHandleRequest(data: Request) = data.uri.scheme == SCHEME_PNAME

  override fun load(request: Request, networkPolicy: Int): Result? {
    val packageName = request.uri.host.orEmpty()
    val user = userCache.findById(request.uri.getQueryParameter(PARAM_USER_ID).orEmpty().toInt())
    return Result(getFullResIcon(packageName, user), Picasso.LoadedFrom.DISK)
  }

  private fun getFullResIcon(packageName: String, user: UserHandle): Bitmap {
    try {
      val icon = launcherApps.getActivityList(packageName, user).first().getIcon(dpi)
      val badgedIcon = if (user.isMain) icon else packageManager.getUserBadgedIcon(icon, user)
      return badgedIcon.toBitmap()
    }
    catch (ignored: PackageManager.NameNotFoundException) { }
    catch (ignored: Resources.NotFoundException) { }

    return defaultAppIcon
  }
}
