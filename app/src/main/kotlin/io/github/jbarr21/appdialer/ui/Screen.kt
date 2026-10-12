package io.github.jbarr21.appdialer.ui

import android.os.Parcelable
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object MainCircuitScreen : Screen, Parcelable

@Parcelize
data object SettingsCircuitScreen : Screen, Parcelable

@Parcelize
data object InfoCircuitScreen : Screen, Parcelable
