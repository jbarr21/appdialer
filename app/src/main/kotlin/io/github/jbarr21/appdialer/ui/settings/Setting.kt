package io.github.jbarr21.appdialer.ui.settings

import androidx.annotation.DrawableRes

data class Setting(
  val title: String,
  val description: String,
  @DrawableRes val iconRes: Int
)