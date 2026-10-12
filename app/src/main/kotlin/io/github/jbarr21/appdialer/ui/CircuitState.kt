package io.github.jbarr21.appdialer.ui

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import io.github.jbarr21.appdialer.ui.info.InfoViewModel
import io.github.jbarr21.appdialer.ui.main.MainViewModel
import io.github.jbarr21.appdialer.ui.settings.SettingsViewModel

data class MainCircuitState(
  val viewModel: MainViewModel,
  val navigator: Navigator,
) : CircuitUiState

data class SettingsCircuitState(
  val viewModel: SettingsViewModel,
  val navigator: Navigator,
) : CircuitUiState

data class InfoCircuitState(
  val viewModel: InfoViewModel,
  val navigator: Navigator,
) : CircuitUiState
