package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

expect fun getScreenHeightPx(): Int
expect fun getScreenWidthPx(): Int
expect fun getScreenHeightDp(): Dp
expect fun getScreenWidthDp(): Dp
expect fun Modifier.statusBarsPadding(): Modifier
expect fun Modifier.navigationBarsPadding(): Modifier
expect fun Modifier.systemBarsPadding(): Modifier

enum class Platform {
  IOS,
  Android
}

expect fun <T> T.platformIs(platform: Platform, block: T.() -> T): T