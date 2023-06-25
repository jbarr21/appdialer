package io.github.jbarr21.appdialer.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@SuppressLint("NewApi")
@Composable
fun AppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  MaterialTheme(
    colorScheme = when {
      dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
      dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
      !dynamicColor && darkTheme -> darkColorScheme()
      !dynamicColor && !darkTheme -> lightColorScheme()
      else -> lightColorScheme()
    },
    content = content,
  )
}
