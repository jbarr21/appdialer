package io.github.jbarr21.appdialer.ui.main

import android.os.Process
import androidx.compose.ui.graphics.Color
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.DialerButton

object MainPreviewData {
  val previewApp = App(
    "Name",
    "com.foo",
    "BarActivity",
    Process.myUserHandle(),
    iconColor = randomColor()
  )

  val previewApps = (0 until 10).map {
    previewApp.copy(
      name = "${previewApp.name} $it",
      iconColor = randomColor()
    )
  }

  val buttons = listOf(DialerButton(label = "CLEAR*")) + (0 until 8).map { digit ->
    DialerButton(
      digit = digit + 2,
      letters = (0 until 3).map {
        ('A'.toInt() + digit * 3 + it).toChar()
      }.joinToString(separator = ""))
  }

  val buttonColors = (0 until 5).map { Color(randomColor()) }

  fun randomColor(): Int {
    return android.graphics.Color.rgb((0..256).random(), (0..256).random(), (0..256).random())
  }
}