package com.glowka.rafal.topmusic.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun <T : Any> makeApiCall(
  apiCall: suspend () -> T
): Result<T> {
  return try {
    withContext(Dispatchers.IO) {
      Result.success(apiCall.invoke())
    }
  } catch (throwable: Throwable) {
    Result.failure(exception = throwable)
  }
}