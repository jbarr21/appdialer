package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
  apps: State<List<App>>,
  buttons: List<DialerButton>,
  buttonColors: List<Color> = emptyList(),
  query: State<String> = mutableStateOf(""),
  selectedApp: State<App?> = mutableStateOf(null),
  appLongClickActions: List<BottomSheetItem> = emptyList(),
  onAppClicked: (App) -> Unit = {},
  onAppLongClicked: (App?) -> Unit = {},
  onDialerClicked: (DialerButton) -> Unit = {},
  onDialerLongClicked: (DialerButton) -> Unit = {}
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val onDialerLongClickedDecorated: (DialerButton) -> Unit = {
    onDialerLongClicked(it)
    if (it.isInfoButton) {
      scope.launch {
        snackbarHostState.showSnackbar(
          message = "Found ${apps.value.size} apps",
          duration = SnackbarDuration.Short
        )
      }
    } else if (it.isRefreshButton) {
      scope.launch {
        snackbarHostState.showSnackbar(
          message = "Refreshing...",
          duration = SnackbarDuration.Short
        )
      }
    }
  }

  Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
    Stack {
      AppGrid(apps = apps, query = query, onClick = onAppClicked, onLongClick = onAppLongClicked)
      Box(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {
        DialerGrid(buttons = buttons, buttonColors = buttonColors, onClick = onDialerClicked, onLongClick = onDialerLongClickedDecorated)
      }
      SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { Snackbar(text = { Text(text = it.message, style = MaterialTheme.typography.body2) }) },
        modifier = Modifier.fillMaxSize().padding(8.dp).align(alignment = Alignment.TopCenter)
      )
      selectedApp.value?.let {
        MainAppBottomSheet(it, appLongClickActions, onDismiss = { onAppLongClicked(null) })
      }
    }
  }
}
