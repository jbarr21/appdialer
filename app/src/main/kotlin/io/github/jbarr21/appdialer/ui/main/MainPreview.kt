package io.github.jbarr21.appdialer.ui.main

import android.os.Process
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.ui.tooling.preview.Preview
import io.github.jbarr21.appdialer.R
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
    MainScreen(apps = mutableStateOf(previewApps), buttons = buttons)
  }
}

@Preview
@Composable
fun MainPreviewLight() {
  AppTheme(darkTheme = false) {
    MainScreen(apps = mutableStateOf(previewApps), buttons = buttons)
  }
}

@Preview
@Composable
fun MainPreviewModal() {
  AppTheme(darkTheme = false) {
    MainScreen(apps = mutableStateOf(previewApps), buttons = buttons)
  }
}

@Preview(widthDp = 300, heightDp = 400)
@Composable
fun MainAppBottomSheet() {
  MainAppBottomSheet(previewApp, actions = listOf(
    BottomSheetItem("Uninstall", R.drawable.ic_delete_black_24dp),
    BottomSheetItem("App Info", R.drawable.ic_info_black_24dp),
    BottomSheetItem("Play Store", R.drawable.ic_local_grocery_store_black_24dp)
  ))
}

@Preview
@Composable
fun AppGridPreview() {
  AppGrid(mutableStateOf(previewApps))
}

@Preview(widthDp = 300, heightDp = 300)
@Preview
@Composable
fun EmptyAppGridPreview() {
  AppGrid(mutableStateOf(emptyList()), query = mutableStateOf("xyz")) {}
}

@Preview(widthDp = 150)
@Composable
fun AppItemPreview() {
  Column {
    AppItem(previewApp) {}
    AppItem(previewApp.copy(name = "Application Name"))
    AppItem(previewApp.copy(name = "Really Long Application Name"))
  }
}

@Preview
@Composable
fun DialerGridPreview() {
  DialerGrid(buttons = buttons)
}

@Preview
@Composable
fun DialerItemPreview() {
  Column {
    DialerItem(DialerButton(digit = 5, letters = "JKL"))
    DialerItem(DialerButton(digit = -1, label = "CLEAR*"))
  }
}
