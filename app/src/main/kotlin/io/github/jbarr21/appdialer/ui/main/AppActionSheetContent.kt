package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.SimpleListItem
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData.previewApp
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

@Composable
fun AppActionSheetContent(
  app: App,
  actions: List<SimpleListItem<App>>,
  onActionClick: () -> Unit = {},
  onDismiss: () -> Unit = {}
) {
  Box(modifier = Modifier.fillMaxWidth()) {
    Surface(
      modifier = Modifier
        .navigationBarsPadding()
        .wrapContentHeight(align = Alignment.Bottom)
        .fillMaxWidth()
    ) {
      Column(verticalArrangement = Arrangement.Bottom) {
        Text(
          text = app.label,
          color = MaterialTheme.colorScheme.primary,
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.padding(16.dp)
        )
        actions.forEach {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = {
                onDismiss()
                it.action(app)
                onActionClick()
              })
              .padding(16.dp)
          ) {
            Image(
              painter = painterResource(id = it.iconRes),
              colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
              contentDescription = null,
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

@ThemePreviews
@Composable
fun AppActionSheetContent() {
  AppTheme {
    AppActionSheetContent(previewApp, actions = listOf(
      SimpleListItem<App>("Uninstall", iconRes = R.drawable.ic_delete_black_24dp),
      SimpleListItem<App>("App Info", iconRes = R.drawable.ic_info_black_24dp),
      SimpleListItem<App>("Play Store", iconRes = R.drawable.ic_local_grocery_store_black_24dp)
    ))
  }
}
