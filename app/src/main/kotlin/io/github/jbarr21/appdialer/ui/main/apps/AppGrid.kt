package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import io.github.jbarr21.appdialer.data.App

@Composable
fun AppGrid(
  apps: State<List<App>>,
  query: State<String> = mutableStateOf(""),
  numColumns: Int = 4,
  onSelected: (App) -> Unit
) {
  val chunkedList = apps.value.chunked(numColumns)
  LazyColumnFor(chunkedList, modifier = Modifier.fillMaxHeight()) { rowApps ->
    Row {
      rowApps.forEach { app ->
        Box(modifier = Modifier.weight(1f)) {
          AppItem(app, onSelected)
        }
      }

      (0 until (numColumns - rowApps.size)).forEach {
        Box(modifier = Modifier.weight(1f))
      }
    }
  }
}
