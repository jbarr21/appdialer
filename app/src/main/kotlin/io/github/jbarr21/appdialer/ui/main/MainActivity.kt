package io.github.jbarr21.appdialer.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.coordinators.Coordinators
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.app.AppDialerApplication

class MainActivity : AppCompatActivity() {

  private val mainScope: MainScope by lazy { AppDialerApplication.component(this).mainScope(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Coordinators.bind(findViewById(android.R.id.content)) { mainScope.coordinator() }
  }
}
