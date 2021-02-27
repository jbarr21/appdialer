package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.main.MainPreviewData.previewApp

@Composable
fun AppItem(
  app: App,
  onClick: (App) -> Unit = {},
  onLongClick: (App) -> Unit = {}
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = { onClick(app) }, onLongClick = { onLongClick(app) })
      .background(color = Color(app.iconColor))
      .padding(all = 8.dp)
  ) {
    CoilImage(
      data = app.iconUri.toString(),
      modifier = Modifier.size(48.dp),
      contentDescription = null,
      loading = {
        Box(modifier = Modifier.background(Color.DarkGray).clip(CircleShape))
      }
    )
    Text(
      text = app.label,
      color = MaterialTheme.colors.onSurface,
      style = MaterialTheme.typography.body2,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1,
      modifier = Modifier.padding(top = 8.dp)
    )
  }
}

@Preview(widthDp = 150)
@Composable
fun AppItemPreview() {
  Column {
    AppItem(previewApp) {}
    AppItem(previewApp.copy(name = "Application Name"))
    AppItem(previewApp.copy(name = "Really Long Application Name"))
  }
}
