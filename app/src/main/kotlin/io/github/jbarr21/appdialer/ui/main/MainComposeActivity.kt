package io.github.jbarr21.appdialer.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.util.ActivityLauncher
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : AppCompatActivity() {

  @Inject lateinit var mainViewModelFactory: MainViewModel.Factory
  @Inject lateinit var activityLauncher: ActivityLauncher

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val mainViewModel by lazy { mainViewModelFactory.create(MainViewModel::class.java) }

    mainViewModel.allApps.observe(this) { apps ->
      setContent {
        AppTheme {
          Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            AppGrid(
              apps = apps,
              onSelected = {
                activityLauncher.startMainActivity(it)
                finishAndRemoveTask()
              }
            )
          }
        }
      }
    }
  }
}
