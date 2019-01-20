package io.github.jbarr21.appdialer.util

import android.app.ActivityManager
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.UserHandle
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.drawable.DrawableResource
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.data.isMain

class AppIconDecoder(
  private val activityManager: ActivityManager,
  private val launcherApps: LauncherApps,
  private val packageManager: PackageManager,
  private val userCache: UserCache
) : ResourceDecoder<Uri, Drawable> {

  companion object {
    const val SCHEME_PNAME = "pname"
    const val PARAM_USER_ID = "userId"
  }

  val id: String = "AppInfoToDrawable"

  private val dpi by lazy { activityManager.launcherLargeIconDensity }
  private val defaultAppIcon by lazy {
    Resources.getSystem().getDrawableForDensity(android.R.mipmap.sym_def_app_icon, dpi)
  }

  override fun handles(source: Uri, options: Options) = source.scheme == SCHEME_PNAME

  override fun decode(source: Uri, width: Int, height: Int, options: Options): Resource<Drawable>? {
    val packageName = source.host.orEmpty()
    val user = userCache.findById(source.getQueryParameter(PARAM_USER_ID).orEmpty().toInt())
    val icon = getFullResIcon(packageName, user)
    return object : DrawableResource<Drawable>(icon) {
      override fun getSize() = icon.toBitmap().byteCount
      override fun getResourceClass() = Drawable::class.java
      override fun recycle() { /* not from our pool */ }
    }
  }

  private fun getFullResIcon(packageName: String, user: UserHandle): Drawable {
    try {
      val icon = launcherApps.getActivityList(packageName, user).first().getIcon(dpi)
      return if (user.isMain) icon else packageManager.getUserBadgedIcon(icon, user)
    }
    catch (ignored: PackageManager.NameNotFoundException) { }
    catch (ignored: Resources.NotFoundException) { }

    return defaultAppIcon
  }
}