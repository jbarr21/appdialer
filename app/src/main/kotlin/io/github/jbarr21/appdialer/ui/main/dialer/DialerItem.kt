package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialerItem(
  button: DialerButton,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  Text(
    text = button.label.toString(),
    textAlign = TextAlign.Center,
    style = MaterialTheme.typography.h6,
    modifier = Modifier
      .combinedClickable(
        onClick = { onClick(button) },
        onLongClick = { onLongClick(button) }
      )// indication = rememberRipple(bounded = false), interactionState = InteractionState()
      .fillMaxWidth()
      .heightIn(96.dp)
      .wrapContentHeight(align = Alignment.CenterVertically)
  )
}

@Preview
@Composable
fun DialerItemPreview() {
  Column {
    DialerItem(DialerButton(digit = 5, letters = "JKL"))
    DialerItem(DialerButton(digit = -1, label = "CLEAR*"))
  }
}
