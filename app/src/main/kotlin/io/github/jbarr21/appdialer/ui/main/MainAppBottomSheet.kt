package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.App

@Composable
fun MainAppBottomSheet(
  app: App,
  actions: List<Pair<String, (App) -> Unit>>
) {
  Surface(
    color = Color.Black.copy(alpha = 0.5f),
    modifier = Modifier.fillMaxSize()
  ) {
    Surface(
      color = MaterialTheme.colors.surface,
      elevation = 8.dp,
      modifier = Modifier.wrapContentHeight(align = Alignment.Bottom).fillMaxWidth()
    ) {
      Column(verticalArrangement = Arrangement.Bottom) {
        Text(
          text = app.label,
          style = MaterialTheme.typography.h6,
          modifier = Modifier.padding(16.dp).clickable(onClick = {}, indication = null)
        )
        actions.forEach { (name, action) ->
          Text(
            text = name,
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = { action(app) })
              .padding(16.dp)
          )
        }
      }
    }
  }
}
