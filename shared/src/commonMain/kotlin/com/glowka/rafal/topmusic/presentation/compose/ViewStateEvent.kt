package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger

sealed interface ViewStateEvent<T> {
  class Idle<T> : ViewStateEvent<T>
  data class Triggered<T>(val data: T) : ViewStateEvent<T>
}

fun <T> ViewStateEvent<T>.isTriggered(block: (T) -> Unit) {
  if (this is ViewStateEvent.Triggered<T>) {
    block(this.data)
  }
}

fun <T> trigger(data: T) = ViewStateEvent.Triggered(data)
fun <T> idle() = ViewStateEvent.Idle<T>()
fun <T> consumed() = idle<T>()

@Composable
fun <T> ViewStateEventCheck(
  event: ViewStateEvent<T>,
  onConsume: (T) -> Unit,
  onTriggeredAction: suspend (T) -> Unit,
) {
  LaunchedEffect(key1 = event) {
    if (event is ViewStateEvent.Triggered<T>) {
      Logger.d("draw state triggered")
      onTriggeredAction(event.data)
      onConsume(event.data)
    }
  }
}
