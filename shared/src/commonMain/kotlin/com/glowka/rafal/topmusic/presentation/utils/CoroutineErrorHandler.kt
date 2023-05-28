package com.glowka.rafal.topmusic.presentation.utils

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class CoroutineErrorHandler :
  AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
  override fun handleException(context: CoroutineContext, exception: Throwable) {
    Logger.e("Coroutine exception", exception)
  }
}
