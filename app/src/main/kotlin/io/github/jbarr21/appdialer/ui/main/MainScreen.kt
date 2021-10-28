package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.ui.AppTheme
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.ui.main.dialer.DialerGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
  viewModel: MainViewModel = viewModel(),
  navController: NavController
) {
  val context = LocalContext.current
  Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
    val scope = rememberCoroutineScope()
    val sheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)
    ModalBottomSheetLayout(
      scrimColor = Color.Black.copy(alpha = 0.5f),
      sheetState = sheetState,
      sheetContent = {
        if (viewModel.selectedApp.value != null) {
          MainAppBottomSheet(
            viewModel.selectedApp.value!!,
            viewModel.appLongClickActions,
            onActionClick = { viewModel.selectedApp.value = null },
            onDismiss = { viewModel.onAppLongClicked(null, sheetState) }
          )
        } else {
          Box(modifier = Modifier.defaultMinSize(minHeight = 1.dp))
        }
      }
    ) {
      val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Expanded)
      )
      BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetPeekHeight = 96.dp,
        sheetContent = {
          DialerGrid(
            buttons = viewModel.dialerLabels,
            buttonColors = viewModel.buttonColors,
            onClick = viewModel.onDialerClicked,
            onLongClick = { viewModel.onDialerLongClickedDecorated(it, navController) })
        }
      ) {
        AppGrid(
          apps = viewModel.filteredApps,
          query = viewModel.queryText,
          isRefreshing = viewModel.isRefreshing,
          onClick = { viewModel.onAppClicked(context, it) },
          onLongClick = {
            val app = viewModel.onAppLongClicked(it, sheetState)
            app?.let {
              scope.launch { sheetState.show() }
            }
          },
          onRefresh = { viewModel.loadApps(false) }
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
