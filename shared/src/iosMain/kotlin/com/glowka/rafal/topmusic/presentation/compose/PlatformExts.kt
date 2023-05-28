package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.pointed
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen

private var screenWidth = 0
private var screenHeight = 0
private var screenWidthDp = 0.dp
private var screenHeightDp = 0.dp
actual fun getScreenHeightPx() = screenHeight
actual fun getScreenWidthPx() = screenWidth
actual fun getScreenHeightDp() = screenHeightDp
actual fun getScreenWidthDp() = screenWidthDp

private object StatusBarOrientations {
  const val portrait = 1L
  const val portraitUpsideDown = 2L
  const val landscapeLeft = 3L
  const val landscapeRight = 4L
}

@Suppress("MagicNumber")
actual fun Modifier.statusBarsPadding(): Modifier {
  val barHeight = UIApplication.sharedApplication.statusBarFrame.size.dp * 1.25f
  var top = 0.dp
  var bottom = 0.dp
  var left = 0.dp
  var right = 0.dp
  when (UIApplication.sharedApplication.statusBarOrientation) {
    StatusBarOrientations.portrait -> top = barHeight
    StatusBarOrientations.portraitUpsideDown -> bottom = barHeight
    StatusBarOrientations.landscapeLeft -> left = barHeight
    StatusBarOrientations.landscapeRight -> right = barHeight
  }

  return this.padding(top = top, bottom = bottom, start = left, end = right)
}

actual fun Modifier.navigationBarsPadding(): Modifier = this

@Suppress("MagicNumber")
actual fun Modifier.systemBarsPadding(): Modifier {
  val barHeight = UIApplication.sharedApplication.statusBarFrame.size.dp * 1.25f
  var top = 0.dp
  var bottom = 0.dp
  var left = 0.dp
  var right = 0.dp
  when (UIApplication.sharedApplication.statusBarOrientation) {
    StatusBarOrientations.portrait -> top = barHeight
    StatusBarOrientations.portraitUpsideDown -> bottom = barHeight
    StatusBarOrientations.landscapeLeft -> left = barHeight
    StatusBarOrientations.landscapeRight -> right = barHeight
  }

  return this.padding(top = top, bottom = bottom, start = left, end = right)
}

actual fun <T> T.platformIs(platform: Platform, block: T.() -> T): T {
  if (platform == Platform.IOS) return this.block()
  return this
}

// I'm not sure of this calculations. Should be verified how compose do it.
fun initScreenData() {
  val scale = UIScreen.mainScreen.scale
  val size = UIScreen.mainScreen.applicationFrame.getPointer(MemScope()).pointed.size
  // val sizePixels = UIScreen.mainScreen.nativeBounds.getPointer(MemScope()).pointed.size // Screen in pixels
  screenHeight = (size.height * scale).toInt()
  screenWidth = (size.width * scale).toInt()
  screenHeightDp = size.height.dp
  screenWidthDp = size.width.dp
}