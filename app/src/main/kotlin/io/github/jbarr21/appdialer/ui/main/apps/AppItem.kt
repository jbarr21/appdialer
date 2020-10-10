package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.Box
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage
import io.github.jbarr21.appdialer.data.App

@Composable
fun AppItem(app: App, onSelected: (App) -> Unit) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = { onSelected(app) })
      .background(color = Color(app.iconColor))
      .padding(all = 8.dp)
  ) {
    CoilImage(
      data = app.iconUri,
      modifier = Modifier.size(48.dp)
    ) { imageState ->
      when (imageState) {
        is ImageLoadState.Success -> {
          MaterialLoadingImage(
            result = imageState,
            fadeInEnabled = true,
            fadeInDurationMs = 600,
          )
        }
        else -> Box(backgroundColor = Color.DarkGray, shape = CircleShape)
      }
    }
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
