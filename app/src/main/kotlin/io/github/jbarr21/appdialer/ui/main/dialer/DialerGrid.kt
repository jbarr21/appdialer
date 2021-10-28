package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData.buttonColors
import io.github.jbarr21.appdialer.ui.main.PreviewData.buttons

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialerGrid(
  buttons: List<DialerButton>,
  buttonColors: List<Color> = emptyList(),
  numColumns: Int = 3,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  Surface(
    color = MaterialTheme.colors.surface,
    elevation = 2.dp,
    modifier = Modifier.shadow(32.dp)
  ) {
    Column {
      LazyVerticalGrid(cells = GridCells.Fixed(numColumns), content = {
        itemsIndexed(buttons) { _, button ->
          DialerItem(button, onClick, onLongClick)
        }
      })
     ColorButtons(buttonColors)
    }
  }
}

@Preview
@Composable
fun DialerGridPreview() {
  AppTheme(darkTheme = true) {
    DialerGrid(buttons = buttons, buttonColors = buttonColors)
  }
}
