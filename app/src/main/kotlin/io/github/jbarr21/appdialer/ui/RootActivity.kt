package io.github.jbarr21.appdialer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import io.github.jbarr21.appdialer.app.AppDialerApplication

class RootActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      AppTheme {
        AppDialerApp((application as AppDialerApplication).appGraph)
      }
    }
  }
}
