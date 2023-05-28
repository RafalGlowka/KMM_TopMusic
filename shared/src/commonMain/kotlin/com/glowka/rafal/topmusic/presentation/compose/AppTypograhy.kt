package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

var appTypography: Typography? = null

@Composable
fun getAppTypography(): Typography {
  appTypography?.let {
    return it
  }
  return MaterialTheme.typography
}

expect suspend fun fontResource(
  identifier: String,
  fontFile: String,
  weight: FontWeight,
  style: FontStyle
): Font