package io.github.jbarr21.appdialer.data

data class DialerButton(
  val digit: Int = 0,
  val letters: String = "",
  val label: CharSequence = "$digit $letters".toUpperCase()
) {
  val isClearButton = "clear" in label.toString().toLowerCase()
  val isInfoButton = "i" in label.toString().toLowerCase()
  val isRefreshButton = "r" in label.toString().toLowerCase()

}

fun List<DialerButton>.asText() = map { it.letters.first().toString() }.joinToString(separator = "")
