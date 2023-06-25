package io.github.jbarr21.appdialer.ui.main.dialer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

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

  CompositionLocalProvider(LocalIndication provides rememberRipple(bounded = false, radius = 64.dp)) {
    Text(
      text = annotatedText,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .combinedClickable(
          onClick = { onClick(button) },
          onLongClick = { onLongClick(button) },
        )
        .fillMaxWidth()
        .heightIn(96.dp)
        .wrapContentHeight(align = Alignment.CenterVertically)
    )
  }
}

internal class ButtonProvider : PreviewParameterProvider<DialerButton> {
  override val values = listOf(
    4 to "GHI",
    5 to "JKL",
    -1 to "CLEAR*"
  ).map { (digit, letters) -> DialerButton(digit = digit, letters = letters) }.asSequence()
}

@ThemePreviews
@Composable
fun DialerItemPreview(@PreviewParameter(ButtonProvider::class) button: DialerButton) {
  AppTheme {
    DialerItem(button)
  }
}
