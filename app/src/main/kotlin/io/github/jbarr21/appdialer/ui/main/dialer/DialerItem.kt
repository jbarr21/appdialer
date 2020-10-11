package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DialerItem(button: DialerButton, onClick: (DialerButton) -> Unit) {
  Text(
    text = button.label.toString(),
    textAlign = TextAlign.Center,
    style = MaterialTheme.typography.h6,
    modifier = Modifier
      .clickable(onClick = { onClick(button) }, indication = RippleIndication(bounded = false))
      .fillMaxWidth()
      .preferredHeight(96.dp)
      .wrapContentHeight(align = Alignment.CenterVertically)
  )
}