package io.github.jbarr21.appdialer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import io.github.jbarr21.appdialer.app.AppGraph

@Composable
fun AppDialerApp(appGraph: AppGraph) {
  val backStack = rememberSaveableBackStack(MainCircuitScreen)
  val navigator = rememberCircuitNavigator(backStack)
  CircuitCompositionLocals(appGraph.circuit) {
    NavigableCircuitContent(
      navigator = navigator,
      backStack = backStack,
      circuit = appGraph.circuit,
    )
  }
}
