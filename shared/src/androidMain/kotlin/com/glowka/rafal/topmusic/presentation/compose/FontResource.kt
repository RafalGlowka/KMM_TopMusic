package com.glowka.rafal.topmusic.presentation.compose

import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.glowka.rafal.topmusic.di.inject

actual suspend fun fontResource(
  identifier: String,
  fontFile: String,
  weight: FontWeight,
  style: FontStyle
): Font {
  val context: Context by inject()
  val name = fontFile.substringBefore(".")
  val fontRes = context.resources.getIdentifier(name, "font", context.packageName)
  return Font(fontRes, weight, style)
}