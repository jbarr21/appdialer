package io.github.jbarr21.appdialer.ui.dialer

data class DialerButton(val digit: Int = 0, val letters: String = "", val label: CharSequence = "$digit $letters".toUpperCase()) {
  val isClearButton = label.toString().toLowerCase() == "clear"
}
