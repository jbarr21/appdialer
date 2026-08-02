package io.github.jbarr21.appdialer.data

data class DialerButton(
  val digit: Int = 0,
  val letters: String = "",
  val label: CharSequence = "$digit $letters".uppercase()
) {
  val isClearButton = "clear" in label.toString().lowercase()
  val isInfoButton = "i" in label.toString().lowercase()
}

fun List<DialerButton>.asText() = map { it.letters.first().toString() }.joinToString(separator = "")
