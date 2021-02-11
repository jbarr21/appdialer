package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton

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
      .clickable(
        onClick = { onClick(button) },
        onLongClick = { onLongClick(button) },
        indication = rememberRipple(bounded = false),
        interactionState = InteractionState()
      )
      .fillMaxWidth()
      .preferredHeight(96.dp)
      .wrapContentHeight(align = Alignment.CenterVertically)
  )
}
