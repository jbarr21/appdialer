package io.github.jbarr21.appdialer.ui.main.dialer

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.PreviewData

@Composable
fun ColorButtons(
  buttonColors: List<Color> = emptyList()
) {
  val context = LocalContext.current
  val onColorClicked: (Color) -> Unit = {
    Toast.makeText(context, "Clicked $it", Toast.LENGTH_SHORT).show()
  }

  Row(
    modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    buttonColors.forEach { color ->
      FloatingActionButton(
        backgroundColor = color,
        onClick = { onColorClicked(color) },
        modifier = Modifier
          .size(48.dp)
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

@Preview
@Composable
fun ColorButtonsPreview() {
  AppTheme(darkTheme = true) {
    ColorButtons(buttonColors = PreviewData.buttonColors)
  }
}
