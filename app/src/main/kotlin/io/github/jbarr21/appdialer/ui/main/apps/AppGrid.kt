package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.id
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData.previewApps
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

@OptIn(ExperimentalMaterialApi::class)
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
  Surface(modifier = Modifier.fillMaxSize()) {
    val isEmpty = apps.isEmpty() && query.isNotEmpty()
    val isLoading = apps.isEmpty() && query.isEmpty()
    when {
      isLoading -> AppGridLoading()
      isEmpty -> AppGridEmpty(query = query)
      apps.isNotEmpty() -> {
        val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)
        Box(Modifier.pullRefresh(pullRefreshState)) {
          LazyVerticalGrid(
            columns = GridCells.Fixed(numColumns),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
          ) {
            items(items = apps, key = { "${it.packageName}${it.activityName}${it.user.id}" }) { app ->
              AppItem(app, query, onClick, onLongClick)
            }
          }
          PullRefreshIndicator(isRefreshing, pullRefreshState,
            Modifier
              .align(Alignment.TopCenter)
              .statusBarsPadding())
        }
      }
    }
  }
}

@Composable
fun AppGridLoading() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    CircularProgressIndicator(modifier = Modifier.padding(32.dp))
  }
}

@Composable
fun AppGridEmpty(query: String) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    Text(
      text = "No matches for \"${query}\"",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.headlineSmall,
      modifier = Modifier.padding(32.dp)
    )
  }
}

@ThemePreviews
@Composable
fun AppGridPreview() {
  AppTheme {
    AppGrid(previewApps)
  }
}

@ThemePreviews
@Preview(widthDp = 300, heightDp = 300)
@Composable
fun EmptyAppGridPreview() {
  AppTheme {
    AppGrid(emptyList(), query = "xyz") {}
  }
}

@ThemePreviews
@Preview(widthDp = 300, heightDp = 300)
@Composable
fun LoadingAppGridPreview() {
  AppTheme {
    AppGrid(emptyList(), query = "") {}
  }
}
