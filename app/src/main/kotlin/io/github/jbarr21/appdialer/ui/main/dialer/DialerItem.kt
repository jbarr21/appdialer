package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.ui.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialerItem(
  button: DialerButton,
  onClick: (DialerButton) -> Unit = {},
  onLongClick: (DialerButton) -> Unit = {}
) {
  val text = button.label.toString()
  val annotatedText = AnnotatedString.Builder().apply {
    append(text.substringBeforeLast("I"))
    if (text.endsWith("I")) {
      pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
      append("I")
      pop()
    }
  }.toAnnotatedString()

  Text(
    text = annotatedText,
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

private class ButtonProvider : PreviewParameterProvider<DialerButton> {
  override val values = listOf(
    4 to "GHI",
    5 to "JKL",
    -1 to "CLEAR*"
  ).map { (digit, letters) -> DialerButton(digit = digit, letters = letters) }.asSequence()
}

@Preview
@Composable
fun DialerItemPreview(@PreviewParameter(ButtonProvider::class) button: DialerButton) {
  AppTheme(darkTheme = true) {
    DialerItem(button)
  }
}
