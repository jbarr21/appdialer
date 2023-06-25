package io.github.jbarr21.appdialer.ui.main

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.jbarr21.appdialer.ui.main.apps.AppGrid
import io.github.jbarr21.appdialer.ui.main.dialer.DialerGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  viewModel: MainViewModel = viewModel(),
  navController: NavController,
  windowSizeClass: WindowSizeClass,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val appActionSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
    confirmValueChange = { it != SheetValue.PartiallyExpanded }
  )
  val dialerScaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberModalBottomSheetState(
      skipPartiallyExpanded = false,
      confirmValueChange = { it != SheetValue.Hidden }
    )
  )

  val appGrid: @Composable (PaddingValues) -> Unit = @Composable {
    AppGrid(
      apps = viewModel.filteredApps,
      query = viewModel.queryText,
      numColumns = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) 6 else 4,
      isRefreshing = viewModel.isRefreshing,
      onClick = { viewModel.onAppClicked(context, it) },
      onLongClick = {
        val app = viewModel.onAppLongClicked(it)
        app?.let {
          scope.launch { appActionSheetState.show() }
        }
      },
      onRefresh = { viewModel.loadApps(false) }
    )
  }

  val dialer: @Composable ColumnScope.() -> Unit = {
    DialerGrid(
      buttons = viewModel.dialerLabels,
      buttonColors = viewModel.buttonColors,
      onClick = viewModel.onDialerClicked,
      onLongClick = { viewModel.onDialerLongClickedDecorated(it, navController) })
  }

  val appActionSheet: @Composable () -> Unit = {
    viewModel.selectedApp.value?.let { selectedApp ->
      ModalBottomSheet(
        onDismissRequest = { scope.launch {
          viewModel.onAppLongClicked(null)
          appActionSheetState.show()
        }},
        modifier = Modifier.wrapContentHeight(align = Alignment.Bottom),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Horizontal),
        scrimColor = Color.Black.copy(alpha = 0.5f),
      ) {
        AppActionSheetContent(
          selectedApp,
          viewModel.appLongClickActions,
          onActionClick = { viewModel.selectedApp.value = null },
          onDismiss = { viewModel.onAppLongClicked(null) }
        )
      }
    }
  } ?: { }

  MainScreenContent(appGrid, dialer, appActionSheet, appActionSheetState, dialerScaffoldState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
  AppGrid: @Composable (PaddingValues) -> Unit,
  Dialer: @Composable ColumnScope.() -> Unit,
  AppActionSheet: @Composable () -> Unit,
  appActionSheetState: SheetState,
  dialerScaffoldState: BottomSheetScaffoldState,
) {
  Surface(modifier = Modifier.fillMaxSize()) {
    var dialerSheetPeekHeight by remember { mutableStateOf(0.dp) }
    BottomSheetScaffold(
      sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
      sheetPeekHeight = dialerSheetPeekHeight,
      sheetTonalElevation = 4.dp,
      scaffoldState = dialerScaffoldState,
      sheetContent = Dialer,
      content = AppGrid,
    )

    AppActionSheet()

    LaunchedEffect(null) {
      dialerScaffoldState.bottomSheetState.expand()
      dialerSheetPeekHeight = 116.dp

      appActionSheetState.hide()
    }
  }
}
