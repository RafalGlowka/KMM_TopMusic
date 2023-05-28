package com.glowka.rafal.topmusic.presentation.formatter

/**
 * Kotlin stdlib doesn't have a 'String.format()' that's not relying on JVM.
 * https://youtrack.jetbrains.com/issue/KT-25506/Stdlib-Stringformat-in-common
 *
 * To keep things in common code, this "formatter" simply prefixes an Int string with zeros
 * if needed.
 * For example, 1.zeroPrefixed(2) returns "01".
 */
fun Int.zeroPrefixed(
  maxLength: Int,
): String {
  if (this < 0 || maxLength < 1) return ""

  val string = this.toString()
  val currentStringLength = string.length
  return if (maxLength <= currentStringLength) {
    string
  } else {
    val diff = maxLength - currentStringLength
    var prefixedZeros = ""
    repeat(diff) {
      prefixedZeros += "0"
    }
    "$prefixedZeros$string"
  }
}
