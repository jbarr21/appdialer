package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData.buttonColors
import io.github.jbarr21.appdialer.ui.main.PreviewData.buttons
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

@Composable
fun DialerGrid(
  buttons: List<DialerButton>,
  buttonColors: List<Color> = emptyList(),
  numColumns: Int = 3,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  Surface(
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 4.dp,
    modifier = Modifier.shadow(32.dp)
  ) {
    Column {
      LazyVerticalGrid(columns = GridCells.Fixed(numColumns)) {
        items(items = buttons, key = { it.label }) { button ->
          DialerItem(button, onClick, onLongClick)
        }
      }
     ColorButtons(buttonColors)
    }
  }
}

@ThemePreviews
@Composable
fun DialerGridPreview() {
  AppTheme {
    DialerGrid(buttons = buttons, buttonColors = buttonColors)
  }
}
