package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

actual suspend fun fontResource(
  identifier: String,
  fontFile: String,
  weight: FontWeight,
  style: FontStyle
): Font = Font(0, weight, style)