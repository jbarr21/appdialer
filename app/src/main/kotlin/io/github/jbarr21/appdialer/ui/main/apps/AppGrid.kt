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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.toPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.main.PreviewData.previewApps

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppGrid(
  apps: List<App>,
  query: String = "",
  numColumns: Int = 4,
  isRefreshing: Boolean = false,
  onClick: (App) -> Unit = {},
  onLongClick: (App) -> Unit = {},
  onRefresh: () -> Unit = {}
) {
  val insets = LocalWindowInsets.current
  val isEmpty = apps.isEmpty() && query.isNotEmpty()
  val isLoading = apps.isEmpty() && query.isEmpty()
  when {
    isLoading -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        CircularProgressIndicator(modifier = Modifier.padding(32.dp))
      }
    }
    isEmpty -> {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Text(
          text = "No matches for \"${query}\"",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.h5,
          modifier = Modifier.padding(32.dp)
        )
      }
    }
    apps.isNotEmpty() -> {
      SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh
      ) {
        LazyVerticalGrid(
          cells = GridCells.Fixed(numColumns),
          contentPadding = insets.statusBars.toPaddingValues(),
          content = {
            itemsIndexed(apps) { _, app ->
              AppItem(app, query, onClick, onLongClick)
            }
          }
        )
      }
    }
  }
}

@Preview
@Composable
fun AppGridPreview() {
  AppGrid(previewApps)
}

@Preview(widthDp = 300, heightDp = 300)
@Composable
fun EmptyAppGridPreview() {
  AppGrid(emptyList(), query = "xyz") {}
}

@Preview(widthDp = 300, heightDp = 300)
@Composable
fun LoadingAppGridPreview() {
  AppGrid(emptyList(), query = "") {}
}
