package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.App

@Composable
fun MainAppBottomSheet(
  app: App,
  actions: List<BottomSheetItem>,
  onDismiss: () -> Unit = {}
) {
  Surface(
    color = Color.Black.copy(alpha = 0.5f),
    modifier = Modifier.fillMaxSize()
      .clickable(onClick = onDismiss, indication = null)
  ) {
    Surface(
      color = MaterialTheme.colors.surface,
      elevation = 8.dp,
      modifier = Modifier
        .wrapContentHeight(align = Alignment.Bottom)
        .fillMaxWidth()
        .clickable(onClick = {}, indication = null)
    ) {
      Column(verticalArrangement = Arrangement.Bottom) {
        Text(
          text = app.label,
          style = MaterialTheme.typography.h6,
          modifier = Modifier.padding(16.dp)
        )
        actions.forEach { it ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = { it.action(app) })
              .padding(16.dp)
          ) {
            Image(
              asset = vectorResource(id = it.iconRes),
              colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
            )
            Text(
              text = it.label,
              modifier = Modifier.padding(start = 16.dp)
            )
          }
        }
      }
    }
  }
}

data class BottomSheetItem(
  val label: String,
  val iconRes: Int,
  val action: (App) -> Unit = {}
)
