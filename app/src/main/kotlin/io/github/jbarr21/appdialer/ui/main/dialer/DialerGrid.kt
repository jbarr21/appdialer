package io.github.jbarr21.appdialer.ui.main.dialer

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton

@Composable
fun DialerGrid(
  buttons: List<DialerButton>,
  buttonColors: List<Color> = emptyList(),
  numColumns: Int = 3,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  val context = ContextAmbient.current
  val onColorClicked: (Color) -> Unit = {
    Toast.makeText(context, "Clicked $it", Toast.LENGTH_SHORT).show()
  }

  val chunkedList = buttons.chunked(numColumns)
  Surface(
    color = MaterialTheme.colors.surface,
    elevation = 2.dp,
    modifier = Modifier.drawShadow(32.dp)
  ) {
    Column {
      LazyColumnFor(chunkedList) { rowButtons ->
        Row {
          rowButtons.forEach { button ->
            Box(modifier = Modifier.weight(1f)) {
              DialerItem(button, onClick, onLongClick)
            }
          }
        }
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        buttonColors.forEach { color ->
          FloatingActionButton(
            backgroundColor = color,
            onClick = { onColorClicked(color) },
            modifier = Modifier.size(48.dp)
              .clickable(onClick = { onColorClicked(color) }, indication = rememberRipple(bounded = false))
              .padding(all = 16.dp)
          ) { }
        }
      }
    }
  }
}
