package io.github.jbarr21.appdialer.util

import androidx.core.app.NotificationManagerCompat

object Channels {
  val list = listOf(
    NotificationChannelCompat(
      "general",
      "General",
      NotificationManagerCompat.IMPORTANCE_DEFAULT
    )
  )
}

data class NotificationChannelCompat(val id: String, val name: String, val importance: Int)