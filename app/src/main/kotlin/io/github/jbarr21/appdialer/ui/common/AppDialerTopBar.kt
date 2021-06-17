package io.github.jbarr21.appdialer.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import io.github.jbarr21.appdialer.ui.AppTheme

@Composable
fun AppDialerTopBar(
  title: String,
  navController: NavController,
  showStatusBar: Boolean = true,
  actions: @Composable RowScope.() -> Unit = {}
) {
  val appBarColor = Color(0xFF2D2D2D)
  TopAppBar(
    title = { Text(title) },
    backgroundColor = appBarColor,
    contentColor = MaterialTheme.colors.onSurface,
    modifier = if (showStatusBar) Modifier
      .background(appBarColor)
      .statusBarsPadding() else Modifier.padding(0.dp),
    actions = actions,
    navigationIcon = navController.previousBackStackEntry?.let {
      {
        IconButton(onClick = { navController.popBackStack() }) {
          Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
      }
    }
  )
}

@Preview
@Composable
fun AppDialerTopBarPreview() {
  AppTheme(darkTheme = true) {
    AppDialerTopBar(title = "AppDialer", NavController(LocalContext.current))
  }
}
