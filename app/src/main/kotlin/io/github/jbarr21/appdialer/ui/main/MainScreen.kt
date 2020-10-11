package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerGrid

@Composable
fun MainScreen(
  apps: State<List<App>>,
  buttons: List<DialerButton>,
  query: State<String> = mutableStateOf(""),
  onAppClicked: (App) -> Unit,
  onDialerClicked: (DialerButton) -> Unit
) {
  Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
    Stack(alignment = Alignment.BottomCenter) {
      AppGrid(apps = apps, query = query, onSelected = onAppClicked)
      DialerGrid(buttons = buttons, onClick = onDialerClicked)
    }
  }
}
