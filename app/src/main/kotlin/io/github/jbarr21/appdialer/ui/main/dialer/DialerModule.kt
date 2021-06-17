package io.github.jbarr21.appdialer.ui.main.dialer

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.jbarr21.appdialer.data.DialerButton

@InstallIn(ViewModelComponent::class)
@Module
object DialerModule {

  @Provides
  fun dialerLabels(): List<DialerButton> {
    return listOf(DialerButton(label = "CLEAR *")) + keyMappings().map { (digit, letters) ->
      DialerButton(digit = digit, letters = letters)
    }.toList()
  }

  fun keyMappings(): Map<Int, String> = (2 until 10).mapIndexed { index, digit ->
    val fourSet = setOf(7, 9)
    val numLetters = if (digit in fourSet) 4 else 3
    val letters = (0 until numLetters).map {
      val offset = index * 3 + it + fourSet.count { digit > it }
      return@map ('a' + offset)
    }
    return@mapIndexed digit to letters.joinToString(separator = "")
  }.toMap()
}
