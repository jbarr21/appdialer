package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.App

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppGrid(
  apps: State<List<App>>,
  query: State<String> = mutableStateOf(""),
  numColumns: Int = 4,
  onClick: (App) -> Unit = {},
  onLongClick: (App) -> Unit = {}
) {
  val isEmpty = apps.value.isEmpty() && query.value.isNotEmpty()
  val isLoading = apps.value.isEmpty() && query.value.isEmpty()
  when {
    isLoading -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        CircularProgressIndicator(modifier = Modifier.padding(32.dp))
      }
    }
    isEmpty -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Text(
          text = "No matches for \"${query.value}\"",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.h5,
          modifier = Modifier.padding(32.dp)
        )
      }
    }
    apps.value.isNotEmpty() -> {
      LazyVerticalGrid(cells = GridCells.Fixed(numColumns), content = {
        itemsIndexed(apps.value) { _, app ->
          AppItem(app, onClick, onLongClick)
        }
      })
    }
  }
}
