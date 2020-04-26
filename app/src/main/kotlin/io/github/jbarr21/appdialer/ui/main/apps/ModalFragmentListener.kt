package io.github.jbarr21.appdialer.ui.main.apps

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.UserHandle
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.UserCache
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.PARAM_USER_ID

class ModalFragmentListener(
  private val activityLauncher: ActivityLauncher,
  private val userCache: UserCache
) : ModalBottomSheetDialogFragment.Listener {
  override fun onModalOptionSelected(tag: String?, option: Option) {
    tag?.let {
      val uri = Uri.parse(it)
      val packageName = uri.host.orEmpty()
      val user = userCache.findById(uri.getQueryParameter(PARAM_USER_ID)?.toInt() ?: 0)
      when (option.id) {
        R.id.uninstall -> uninstallApp(packageName, user)
        R.id.app_info -> startAppDetails(packageName, user)
        R.id.play_store -> startPlayStore(packageName, user)
      }
    }
  }

  private fun uninstallApp(packageName: String, user: UserHandle) {
    val packageURI = Uri.parse("package:$packageName")
    val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI)
    intent.putExtra(Intent.EXTRA_USER, user)
    activityLauncher.startActivity(intent)
  }

  private fun startAppDetails(packageName: String, user: UserHandle) {
    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    intent.putExtra(Intent.EXTRA_USER, user)
    activityLauncher.startActivity(intent)
  }

  private fun startPlayStore(packageName: String, user: UserHandle) {
    val intent = try {
      Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    } catch (anfe: ActivityNotFoundException) {
      Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
    }
    intent.putExtra(Intent.EXTRA_USER, user)
    activityLauncher.startActivity(intent)
  }
}