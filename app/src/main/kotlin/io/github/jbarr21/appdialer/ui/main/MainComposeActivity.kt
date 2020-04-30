package io.github.jbarr21.appdialer.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.foundation.Text
import androidx.ui.layout.fillMaxSize
import androidx.ui.tooling.preview.Preview

class MainComposeActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center, children = {
        Text("Hello, world!")
      })
    }
  }
}

@Preview
@Composable
fun TestPreview() {
  Text("Hello, Preview!")
}
