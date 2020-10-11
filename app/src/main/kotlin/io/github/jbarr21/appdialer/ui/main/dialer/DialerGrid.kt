package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.unit.dp

@Composable
fun DialerGrid(
  buttons: List<DialerButton>,
  numColumns: Int = 3,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  val chunkedList = buttons.chunked(numColumns)
  Surface(
    color = MaterialTheme.colors.surface,
    elevation = 2.dp,
    modifier = Modifier.drawShadow(32.dp)
  ) {
    LazyColumnFor(chunkedList) { rowButtons ->
      Row {
        rowButtons.forEach { button ->
          Box(modifier = Modifier.weight(1f)) {
            DialerItem(button, onClick, onLongClick)
          }
        }
      }
    }
  }
}
