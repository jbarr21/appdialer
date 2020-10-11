package io.github.jbarr21.appdialer.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.dialer.DialerButton
import io.github.jbarr21.appdialer.ui.settings.SettingsActivity
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.Vibrator
import javax.inject.Inject

@AndroidEntryPoint
class MainComposeActivity : AppCompatActivity() {

  @Inject lateinit var activityLauncher: ActivityLauncher
  @Inject lateinit var dialerLabels: List<DialerButton>
  @Inject lateinit var mainViewModelFactory: MainViewModel.Factory
  @Inject lateinit var vibrator: Vibrator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val mainViewModel by lazy { mainViewModelFactory.create(MainViewModel::class.java) }

    val onAppClicked: (App) -> Unit = {
      vibrator.vibrate()
      activityLauncher.startMainActivity(it)
      finishAndRemoveTask()
    }

    val onDialerClicked: (DialerButton) -> Unit = {
      vibrator.vibrate()
      if (it.isClearButton) {
        mainViewModel.clearQuery()
      } else {
        mainViewModel.addToQuery(it)
      }
    }

    val onDialerLongClicked: (DialerButton) -> Unit = {
      vibrator.vibrate()
      when {
        it.isClearButton -> activityLauncher.startActivity(Intent(this, SettingsActivity::class.java))
      }
    }

    setContent {
      AppTheme {
        MainScreen(
          apps = mainViewModel.filteredApps.observeAsState(emptyList()),
          buttons = dialerLabels,
          query = mainViewModel.queryText(),
          onAppClicked = onAppClicked,
          onDialerClicked = onDialerClicked,
          onDialerLongClicked = onDialerLongClicked
        )
      }
    }
  }
}
