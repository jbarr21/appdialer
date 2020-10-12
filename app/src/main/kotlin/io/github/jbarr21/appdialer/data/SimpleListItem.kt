package io.github.jbarr21.appdialer.data

import androidx.annotation.DrawableRes

data class SimpleListItem<T>(
  val label: String,
  val description: String? = null,
  @DrawableRes val iconRes: Int,
  val action: (T) -> Unit = {}
)
