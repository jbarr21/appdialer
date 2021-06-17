package io.github.jbarr21.appdialer.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.jbarr21.appdialer.ui.info.InfoScreen
import io.github.jbarr21.appdialer.ui.main.MainScreen
import io.github.jbarr21.appdialer.ui.settings.SettingsScreen

@Composable
fun AppDialerApp() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Screen.Main.toString()) {
    composable(Screen.Main.toString()) { MainScreen(hiltViewModel(), navController) }
    composable(Screen.Settings.toString()) { SettingsScreen(hiltViewModel(), navController) }
    composable(Screen.Info.toString()) { InfoScreen(hiltViewModel(), navController) }
  }
}
