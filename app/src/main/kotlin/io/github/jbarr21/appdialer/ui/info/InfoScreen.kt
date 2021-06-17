package io.github.jbarr21.appdialer.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.common.AppDialerTopBar

@Composable
fun InfoScreen(viewModel: InfoViewModel = viewModel(), navController: NavController) {
  Scaffold(topBar = { AppDialerTopBar(title = "AppDialer Info", navController = navController) }) {
    InfoContent(viewModel.appStats)
  }
}

@Composable
fun InfoContent(appStats: AppStats) {
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
      if (appStats.isLoaded) {
        with (appStats) {
          listOf("Total = $totalCount",
            "Unique = $uniqueAppCount",
            "Main = $mainAppCount",
            "Work = $workAppCount"
          ).let {
            Text(it.joinToString("\n"))
          }
        }
      } else {
        CircularProgressIndicator()
      }
    }
  }
}

@Preview
@Composable
fun InfoScreenPreview() {
  AppTheme(darkTheme = true) {
    InfoContent(AppStats(142, 121, 100, 42))
  }
}

@Preview
@Composable
fun InfoScreenLoadingPreview() {
  AppTheme(darkTheme = true) {
    InfoContent(AppStats(-1, -1, -1, -1))
  }
}
