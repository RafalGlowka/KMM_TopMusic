@file:Suppress("NOTHING_TO_INLINE")

package com.glowka.rafal.topmusic.presentation.utils

import android.util.Log

inline val Any.logTag: String
  get() {
    val tag = this::class.java.simpleName
    if (tag.isEmpty()) return "AnonymousObject"
    return tag
  }

inline fun Any.logD(message: String) {
  Log.d(logTag, message)
}

inline fun Any.logD(message: String, throwable: Throwable) {
  Log.d(logTag, message, throwable)
}

inline fun Any.logE(message: String) {
  Log.e(logTag, message)
}

inline fun Any.logE(message: String, throwable: Throwable) {
  Log.e(logTag, message, throwable)
}