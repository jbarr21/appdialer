package io.github.jbarr21.appdialer.ui.preview

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination

internal class FakeNavController(context: Context) : NavController(context) {
  private var _previousBackStackEntry: NavBackStackEntry? = null

  override val previousBackStackEntry: NavBackStackEntry?
    get() = _previousBackStackEntry

  fun setPreviousBackStackEntry(entry: NavBackStackEntry?) {
    _previousBackStackEntry = entry
  }

  companion object {
    @SuppressLint("RestrictedApi")
    @Composable
    internal fun create(showNavIcon: Boolean = false): NavController {
      return FakeNavController(LocalContext.current).apply {
        if (showNavIcon) {
          setPreviousBackStackEntry(
            NavBackStackEntry.create(context = null, destination = NavDestination("prev"))
          )
        }
      }
    }
  }
}
