package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.ui.main.dialer.DialerGrid

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
  viewModel: MainViewModel = viewModel(),
  navController: NavController
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val context = LocalContext.current

  Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
    Box {
      AppGrid(
        apps = viewModel.filteredApps,
        query = viewModel.queryText,
        isRefreshing = viewModel.isRefreshing,
        onClick = { viewModel.onAppClicked(context, it) },
        onLongClick = viewModel.onAppLongClicked,
        onRefresh = { viewModel.loadApps(false) }
      )
      Box(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {
        DialerGrid(
          buttons = viewModel.dialerLabels,
          buttonColors = viewModel.buttonColors,
          onClick = viewModel.onDialerClicked,
          onLongClick = { viewModel.onDialerLongClickedDecorated(it, navController, snackbarHostState) }
        )
      }
      SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
          .fillMaxSize()
          .padding(8.dp)
          .align(alignment = Alignment.TopCenter)
      )
      viewModel.selectedApp.value?.let {
        MainAppBottomSheet(
          it,
          viewModel.appLongClickActions,
          onActionClick = { viewModel.selectedApp.value = null },
          onDismiss = { viewModel.onAppLongClicked(null) }
        )
      }
    }
  }
}

@Preview
@Composable
fun MainPreview() {
  AppTheme(darkTheme = true) {
    MainScreen(
      viewModel = hiltViewModel(),
      navController = NavController(LocalContext.current)
    )
  }
}

@Preview
@Composable
fun MainPreviewLight() {
  AppTheme(darkTheme = false) {
    MainScreen(
      viewModel = hiltViewModel(),
      navController = NavController(LocalContext.current)
    )
  }
}

@Preview
@Composable
fun MainPreviewModal() {
  AppTheme(darkTheme = false) {
    MainScreen(
      viewModel = hiltViewModel(),
      navController = NavController(LocalContext.current)
    )
  }
}
