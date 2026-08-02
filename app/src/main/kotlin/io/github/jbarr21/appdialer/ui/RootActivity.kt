package io.github.jbarr21.appdialer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import io.github.jbarr21.appdialer.app.AppDialerApplication

class RootActivity : ComponentActivity() {

  @ExperimentalMaterial3WindowSizeClassApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      val windowSizeClass = calculateWindowSizeClass(this)
      AppTheme {
        AppDialerApp(windowSizeClass, (application as AppDialerApplication).appGraph)
      }
    }
  }
}
