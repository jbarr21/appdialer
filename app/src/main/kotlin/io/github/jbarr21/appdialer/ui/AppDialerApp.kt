package io.github.jbarr21.appdialer.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jbarr21.appdialer.ui.info.InfoScreen
import io.github.jbarr21.appdialer.ui.main.MainScreen
import io.github.jbarr21.appdialer.ui.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppDialerApp(windowSizeClass: WindowSizeClass) {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = Screen.Main.toString(),
  ) {
    composable(Screen.Main.toString()) { MainScreen(hiltViewModel(), navController, windowSizeClass) }
    composable(Screen.Settings.toString()) { SettingsScreen(hiltViewModel(), navController) }
    composable(Screen.Info.toString()) { InfoScreen(hiltViewModel(), navController) }
  }
}