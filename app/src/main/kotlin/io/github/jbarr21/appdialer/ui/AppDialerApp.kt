package io.github.jbarr21.appdialer.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.jbarr21.appdialer.ui.info.InfoScreen
import io.github.jbarr21.appdialer.ui.main.MainScreen
import io.github.jbarr21.appdialer.ui.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppDialerApp() {
  val navController = rememberAnimatedNavController()
  AnimatedNavHost(
    navController = navController,
    startDestination = Screen.Main.toString(),
  ) {
    composable(Screen.Main.toString()) { MainScreen(hiltViewModel(), navController) }
    composable(Screen.Settings.toString()) { SettingsScreen(hiltViewModel(), navController) }
    composable(Screen.Info.toString()) { InfoScreen(hiltViewModel(), navController) }
  }
}