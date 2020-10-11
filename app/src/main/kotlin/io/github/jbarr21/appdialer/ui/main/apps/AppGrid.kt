package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.App

@Composable
fun AppGrid(
  apps: State<List<App>>,
  query: State<String> = mutableStateOf(""),
  numColumns: Int = 4,
  onSelected: (App) -> Unit
) {
  val isEmpty = apps.value.isEmpty() && query.value.isNotEmpty()
  val isLoading = apps.value.isEmpty() && query.value.isEmpty()
  when {
    isLoading -> {
      Box(modifier = Modifier.fillMaxHeight(), alignment = Alignment.TopCenter) {
        CircularProgressIndicator(modifier = Modifier.padding(32.dp))
      }
    }
    isEmpty -> {
      Box(modifier = Modifier.fillMaxHeight(), alignment = Alignment.TopCenter) {
        Text(
          text = "No matches for \"${query.value}\"",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.h5,
          modifier = Modifier.padding(32.dp)
        )
      }
    }
    else -> {
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
  }
}
