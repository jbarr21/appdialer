package io.github.jbarr21.appdialer.ui.main

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.jbarr21.appdialer.data.DialerButton

@InstallIn(ActivityComponent::class)
@Module
object MainModule {

  @Provides
  fun dialerLabels(): List<DialerButton> {
    return listOf(DialerButton(label = "CLEAR *")) + keyMappings().map { (digit, letters) ->
      DialerButton(digit = digit, letters = letters)
    }.toList()
  }

  fun keyMappings() = (2 until 10).mapIndexed { index, digit ->
    val fourSet = setOf(7, 9)
    val numLetters = if (digit in fourSet) 4 else 3
    val letters = (0 until numLetters).map {
      val offset = index * 3 + it + fourSet.count { digit > it }
      return@map ('a' + offset)
    }
    return@mapIndexed digit to letters.joinToString(separator = "")
  }.toMap()
}
