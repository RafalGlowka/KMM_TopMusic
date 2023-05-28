package com.glowka.rafal.topmusic.domain.utils

fun <T> ArrayList<T>.push(element: T) {
  add(element)
}

fun <T> ArrayList<T>.pop(): T? {
  if (isEmpty()) return null
  return removeAt(size - 1)
}

fun <T> ArrayList<T>.replace(element: T) {
  pop()
  add(element)
}