package io.github.jbarr21.appdialer.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.preview.BooleanPreviewParameterProvider
import io.github.jbarr21.appdialer.ui.preview.FakeNavController
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDialerTopBar(
  title: String,
  navController: NavController,
  actions: @Composable RowScope.() -> Unit = {},
) {
  TopAppBar(
    title = { Text(title) },
    navigationIcon = {
      navController.previousBackStackEntry?.let {
        IconButton(onClick = { navController.popBackStack() }) {
          Icon(Icons.Default.ArrowBack, contentDescription = null)
        }
      }
    },
    actions = actions,
    colors = topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
  )
}

@ThemePreviews
@Composable
fun AppDialerTopBarPreview(@PreviewParameter(BooleanPreviewParameterProvider::class) showNavIcon: Boolean) {
  AppTheme {
    AppDialerTopBar(title = "AppDialer", FakeNavController.create(showNavIcon = showNavIcon))
  }
}
