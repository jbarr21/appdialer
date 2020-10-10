package io.github.jbarr21.appdialer.ui.main

import android.os.Process
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.ui.tooling.preview.Preview
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.ui.main.apps.AppItem
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.main.dialer.DialerGrid
import io.github.jbarr21.appdialer.ui.main.dialer.DialerItem

private val previewApp = App(
  "Name",
  "com.foo",
  "BarActivity",
  Process.myUserHandle(),
  iconColor = randomColor()
)

private val previewApps = (0 until 10).map {
  previewApp.copy(
    name = "${previewApp.name} $it",
    iconColor = randomColor()
  )
}

private val previewAppsState = object : State<List<App>> {
  override val value: List<App>
    get() = previewApps
}

private val buttons = listOf(DialerButton(label = "CLEAR*")) + (0 until 8).map { digit ->
  DialerButton(
    digit = digit + 2,
    letters = (0 until 3).map {
      ('A'.toInt() + digit * 3 + it).toChar()
    }.joinToString(separator = ""))
}

private fun randomColor(): Int {
  return android.graphics.Color.rgb((0..256).random(), (0..256).random(), (0..256).random())
}

@Preview
@Composable
fun MainPreview() {
  AppTheme(darkTheme = true) {
    MainScreen(apps = previewAppsState, buttons = buttons, onAppClicked = {}, onDialerClicked = {})
  }
}

@Preview
@Composable
fun MainPreviewLight() {
  AppTheme(darkTheme = false) {
    MainScreen(apps = previewAppsState, buttons = buttons, onAppClicked = {}, onDialerClicked = {})
  }
}

@Preview
@Composable
fun AppGridPreview() {
  AppGrid(previewAppsState) {}
}

@Preview(widthDp = 150)
@Composable
fun AppItemPreview() {
  Column {
    AppItem(previewApp) {}
    AppItem(previewApp.copy(name = "Application Name")) {}
    AppItem(previewApp.copy(name = "Really Long Application Name")) {}
  }
}

@Preview
@Composable
fun DialerGridPreview() {
  DialerGrid(buttons = buttons, onClick = {})
}

@Preview
@Composable
fun DialerItemPreview() {
  Column {
    DialerItem(DialerButton(digit = 5, letters = "JKL"), {})
    DialerItem(DialerButton(digit = -1, label = "CLEAR*"), {})
  }
}
