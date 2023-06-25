package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData.previewApp
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppItem(
  app: App,
  query: String = "",
  onClick: (App) -> Unit = {},
  onLongClick: (App) -> Unit = {}
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .combinedClickable(onClick = { onClick(app) }, onLongClick = { onLongClick(app) })
      .background(color = Color(app.iconColor))
      .padding(all = 8.dp)
  ) {
    Box(modifier = Modifier
      .padding(8.dp)
      .fillMaxWidth()
      .aspectRatio(1f)) {

      Image(
        painter = rememberImagePainter(
          data = app.iconUri,
          builder = {
            crossfade(true)
          }
        ),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
      )
    }
    Text(
      text = app.annotatedLabel(query),
      color = MaterialTheme.colorScheme.onSurface,
      style = MaterialTheme.typography.bodyMedium,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1
    )
  }
}

private class AppProvider : PreviewParameterProvider<App> {
  override val values = listOf("Name", "Application Name", "Really Long Application Name")
    .map { previewApp.copy(name = it) }
    .asSequence()
}

@ThemePreviews
@Composable
fun AppItemPreview(@PreviewParameter(AppProvider::class) app: App) {
  AppTheme {
    AppItem(app)
  }
}
