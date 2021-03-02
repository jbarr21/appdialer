package io.github.jbarr21.appdialer.ui.main.dialer

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.ui.main.MainPreviewData.buttonColors
import io.github.jbarr21.appdialer.ui.main.MainPreviewData.buttons

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialerGrid(
  buttons: List<DialerButton>,
  buttonColors: List<Color> = emptyList(),
  numColumns: Int = 3,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  val context = LocalContext.current
  val onColorClicked: (Color) -> Unit = {
    Toast.makeText(context, "Clicked $it", Toast.LENGTH_SHORT).show()
  }

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
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        buttonColors.forEach { color ->
          FloatingActionButton(
            backgroundColor = color,
            onClick = { onColorClicked(color) },
            modifier = Modifier.size(48.dp)
              .clickable(
                onClick = { onColorClicked(color) }
//                , indication = rememberRipple(bounded = false),
//                interactionState = InteractionState()
              )
              .padding(all = 16.dp)
          ) { }
        }
      }
    }
  }
}

@Preview
@Composable
fun DialerGridPreview() {
  DialerGrid(buttons = buttons, buttonColors = buttonColors)
}
