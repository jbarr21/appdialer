package io.github.jbarr21.appdialer.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.data.DialerButton
import io.github.jbarr21.appdialer.ui.settings.SettingsActivity
import io.github.jbarr21.appdialer.util.ActivityLauncher
import io.github.jbarr21.appdialer.util.Vibrator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    val onAppLongClicked: (App?) -> Unit = {
      vibrator.vibrate()
      it?.let { mainViewModel.selectApp(it) } ?: mainViewModel.deselectApp()
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
        it.isRefreshButton -> mainViewModel.loadApps(useCache = false)
      }
    }

    val itemAction: (() -> Unit) -> Unit = {
      it()
      mainViewModel.deselectApp()
    }
    val appLongClickActions = listOf(
      BottomSheetItem("Uninstall", R.drawable.ic_delete_black_24dp) {
        itemAction { activityLauncher.uninstallApp(it.packageName, it.user) }
      },
      BottomSheetItem("App Info", R.drawable.ic_info_black_24dp) {
        itemAction { activityLauncher.startAppDetails(it.packageName, it.user) }
      },
      BottomSheetItem("Play Store", R.drawable.ic_local_grocery_store_black_24dp) {
        itemAction { activityLauncher.startPlayStore(it.packageName, it.user) }
      }
    )

    setContent {
      AppTheme {
        MainScreen(
          apps = mainViewModel.filteredApps.observeAsState(emptyList()),
          buttons = dialerLabels,
          buttonColors = mainViewModel.buttonColors,
          query = mainViewModel.queryText(),
          selectedApp = mainViewModel.selectedApp,
          appLongClickActions = appLongClickActions,
          onAppClicked = onAppClicked,
          onAppLongClicked = onAppLongClicked,
          onDialerClicked = onDialerClicked,
          onDialerLongClicked = onDialerLongClicked
        )
      }
    }
  }
}
