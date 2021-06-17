package io.github.jbarr21.appdialer.ui.main

import android.os.Process
import androidx.compose.ui.graphics.Color
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.data.DialerButton
import kotlin.random.Random

object PreviewData {
  val previewApp = App(
    "Name",
    "com.foo",
    "BarActivity",
    Process.myUserHandle(),
    iconColor = randomColor("Name")
  )

  val previewApps = (0 until 10).map {
    val name = "${previewApp.name} $it"
    previewApp.copy(name = name, iconColor = randomColor(name))
  }

  val buttons = listOf(DialerButton(label = "CLEAR*")) + (0 until 8).map { digit ->
    DialerButton(
      digit = digit + 2,
      letters = (0 until 3).map {
        ('A'.toInt() + digit * 3 + it).toChar()
      }.joinToString(separator = ""))
  }

  val buttonColors = (0 until 5).map { Color(randomColor(('A'.toInt() + it).toString())) }

  private fun randomColor(str: String): Int {
    val random = Random(str.hashCode())
    return android.graphics.Color.rgb(
      (0..256).random(random),
      (0..256).random(random),
      (0..256).random(random)
    )
  }
}
