package io.github.jbarr21.appdialer.data

import android.content.ComponentName
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.graphics.Color.TRANSPARENT
import android.net.Uri
import android.os.UserHandle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import io.github.jbarr21.appdialer.ui.main.dialer.DialerModule
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.PARAM_ACTIVITY_NAME
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.PARAM_USER_ID
import io.github.jbarr21.appdialer.util.AppIconFetcher.Companion.SCHEME_PNAME

// TODO: fine tune color selection
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

  fun annotatedLabel(query: String): AnnotatedString {
    val keyMappings = DialerModule.keyMappings()
    var matching = 0
    while (matching < query.length && matching < label.length
        && keyMappings.keyForValueContainingChar(label[matching]) == keyMappings.keyForValueContainingChar(query[matching])) {
      matching++
    }

    return AnnotatedString.Builder().apply {
      label.substring(0, matching).let {
        if (it.isNotEmpty()) {
          pushStyle(SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold))
          append(it)
          pop()
        }
      }
      append(label.substring(minOf(matching, label.length)))
    }.toAnnotatedString()
  }

  private fun Map<Int, String>.keyForValueContainingChar(ch: Char): Int {
    return entries.firstOrNull { (_, letters) -> ch.toLowerCase() in letters.toLowerCase() }?.key ?: -1
  }

  companion object {
    fun iconUri(packageName: String, activityName: String, user: UserHandle): Uri {
      return Uri.parse("$SCHEME_PNAME://$packageName?$PARAM_ACTIVITY_NAME=$activityName&$PARAM_USER_ID=${user.id}")
    }
  }
}