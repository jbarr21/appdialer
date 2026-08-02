package io.github.jbarr21.appdialer.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jbarr21.appdialer.app.AppGraph
import io.github.jbarr21.appdialer.ui.info.InfoScreen
import io.github.jbarr21.appdialer.ui.main.MainScreen
import io.github.jbarr21.appdialer.ui.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppDialerApp(windowSizeClass: WindowSizeClass, appGraph: AppGraph) {
  val navController = rememberNavController()
  val mainFactory = remember(appGraph) { appGraph.factory { appGraph.mainViewModel } }
  val settingsFactory = remember(appGraph) { appGraph.factory { appGraph.settingsViewModel } }
  val infoFactory = remember(appGraph) { appGraph.factory { appGraph.infoViewModel } }
  NavHost(
    navController = navController,
    startDestination = Screen.Main.toString(),
  ) {
    composable(Screen.Main.toString()) { MainScreen(viewModel(factory = mainFactory), navController, windowSizeClass) }
    composable(Screen.Settings.toString()) { SettingsScreen(viewModel(factory = settingsFactory), navController) }
    composable(Screen.Info.toString()) { InfoScreen(viewModel(factory = infoFactory), navController) }
  }
}

private inline fun <reified T : ViewModel> AppGraph.factory(crossinline create: () -> T) =
  object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = create() as VM
  }
