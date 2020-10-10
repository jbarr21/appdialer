package io.github.jbarr21.appdialer.ui.main.apps

import android.os.Process
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import io.github.jbarr21.appdialer.data.App

val previewApp = App(
  "Name",
  "com.foo",
  "BarActivity",
  Process.myUserHandle(),
  iconColor = randomColor()
)

val previewApps = (0 until 18).map {
  previewApp.copy(
    name = "${previewApp.name} $it",
    iconColor = randomColor()
  )
}

fun randomColor(): Int {
  return android.graphics.Color.rgb((0..256).random(), (0..256).random(), (0..256).random())
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

@Preview(widthDp = 300)
@Composable
fun GridPreview() {
  Column {
    AppGrid(previewApps) {}
  }
}
