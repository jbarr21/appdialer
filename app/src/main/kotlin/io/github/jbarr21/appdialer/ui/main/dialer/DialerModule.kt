package io.github.jbarr21.appdialer.ui.main.dialer

object DialerModule {

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
