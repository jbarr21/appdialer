package io.github.jbarr21.appdialer.ui.main.apps

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData.previewApp

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

      val painter = rememberCoilPainter(request = app.iconUri.toString())
      when (painter.loadState) {
        is ImageLoadState.Success -> {
          Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
          )
        }
        else -> {
          Box(modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(Color.DarkGray))
        }
      }
    }
    Text(
      text = app.annotatedLabel(query),
      color = MaterialTheme.colors.onSurface,
      style = MaterialTheme.typography.body2,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1
    )
  }
}

@Preview(widthDp = 150)
@Composable
fun AppItemPreview() {
  AppTheme(darkTheme = true) {
    Column {
      listOf("Name", "Application Name", "Really Long Application Name")
        .forEach {
          AppItem(previewApp.copy(name = it))
          Spacer(modifier = Modifier.height(16.dp))
        }
      AppItem(previewApp.copy(name = "Calm"), query = "AA")
    }
  }
}
