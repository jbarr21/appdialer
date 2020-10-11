package io.github.jbarr21.appdialer.ui.main.apps

import android.net.Uri
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
        R.id.uninstall -> activityLauncher.uninstallApp(packageName, user)
        R.id.app_info -> activityLauncher.startAppDetails(packageName, user)
        R.id.play_store -> activityLauncher.startPlayStore(packageName, user)
      }
    }
  }
}
