package io.github.jbarr21.appdialer.ui.main.dialer

data class DialerButton(
  val digit: Int = 0,
  val letters: String = "",
  val label: CharSequence = "$digit $letters".toUpperCase()
) {
  val isClearButton = "clear" in label.toString().toLowerCase()
  val isInfoButton = "i" in label.toString().toLowerCase()
}