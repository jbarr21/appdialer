package io.github.jbarr21.appdialer.ui

sealed class Screen(val route: String) {
  object Main : Screen("main")
  object Settings : Screen("settings")
  object Info : Screen("info")

  companion object {
    val items = listOf(
      Main,
      Settings,
      Info
    )
  }
}
