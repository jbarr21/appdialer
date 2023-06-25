package io.github.jbarr21.appdialer.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class BooleanPreviewParameterProvider : PreviewParameterProvider<Boolean> {
  override val values: Sequence<Boolean>
    get() = sequenceOf(false, true)
}
