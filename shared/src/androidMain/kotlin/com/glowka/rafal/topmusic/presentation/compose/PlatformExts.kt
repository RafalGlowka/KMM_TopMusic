package com.glowka.rafal.topmusic.presentation.compose

import android.content.Context
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private var screenWidth = 0
private var screenHeight = 0
private var screenWidthDp = 0.dp
private var screenHeightDp = 0.dp
actual fun getScreenHeightPx() = screenHeight
actual fun getScreenWidthPx() = screenWidth
actual fun getScreenHeightDp() = screenHeightDp
actual fun getScreenWidthDp() = screenWidthDp
actual fun Modifier.statusBarsPadding(): Modifier = statusBarsPadding()
actual fun Modifier.navigationBarsPadding(): Modifier = navigationBarsPadding()
actual fun Modifier.systemBarsPadding(): Modifier = systemBarsPadding()

actual fun <T> T.platformIs(platform: Platform, block: T.() -> T): T {
  if (platform == Platform.Android) return this.block()
  return this
}

fun Context.initScreenData() {
  val displayMetrics = resources.displayMetrics
  screenWidth = displayMetrics.widthPixels
  screenHeight = displayMetrics.heightPixels
  screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).dp
  screenHeightDp = (displayMetrics.heightPixels / displayMetrics.density).dp
}