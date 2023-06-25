package io.github.jbarr21.appdialer.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.common.AppDialerTopBar
import io.github.jbarr21.appdialer.ui.preview.ThemePreviews

@Composable
fun InfoScreen(viewModel: InfoViewModel = viewModel(), navController: NavController) {
  Scaffold(topBar = { AppDialerTopBar(title = "Info", navController = navController) }) {
    InfoContent(viewModel.appStats, Modifier.padding(it))
  }
}

@Composable
fun InfoContent(appStats: AppStats, modifier: Modifier = Modifier) {
  Surface(modifier = Modifier.fillMaxWidth().then(modifier)) {
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

@ThemePreviews
@Composable
fun InfoScreenPreview() {
  AppTheme {
    InfoContent(AppStats(142, 121, 100, 42))
  }
}

@ThemePreviews
@Composable
fun InfoScreenLoadingPreview() {
  AppTheme {
    InfoContent(AppStats(-1, -1, -1, -1))
  }
}
