package io.github.jbarr21.appdialer.data

import android.content.ComponentName
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.graphics.Color.TRANSPARENT
import android.net.Uri
import android.os.UserHandle
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.PARAM_ACTIVITY_NAME
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.PARAM_USER_ID
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.SCHEME_PNAME

// TODO: fine tune color selection
// TODO: use Adaptive Icons
data class App(
  val name: String,
  val packageName: String,
  val activityName: String,
  val user: UserHandle,
  val iconColor: Int = TRANSPARENT
) {
  val label get() = name + if (user.isMain) "" else " (Work)"
  val iconUri get() = iconUri(packageName, activityName, user)
  val launchIntent: Intent
    get() = Intent(ACTION_MAIN)
      .addCategory(Intent.CATEGORY_LAUNCHER)
      .setComponent(ComponentName(packageName, activityName))
      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)

  fun isItemSameAs(other: App): Boolean {
    return user == other.user && packageName == other.packageName && activityName == other.activityName
  }

  companion object {
    fun iconUri(packageName: String, activityName: String, user: UserHandle): Uri {
      return Uri.parse("$SCHEME_PNAME://$packageName?$PARAM_ACTIVITY_NAME=$activityName&$PARAM_USER_ID=${user.id}")
    }
  }
}