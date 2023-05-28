package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.ui.Modifier

actual fun Modifier.statusBarsPadding(): Modifier = Modifier
actual fun Modifier.navigationBarsPadding(): Modifier = Modifier
actual fun Modifier.systemBarsPadding(): Modifier = Modifier

actual fun <T> T.platformIs(platform: Platform, block: T.() -> T) : T {
  return block()
}