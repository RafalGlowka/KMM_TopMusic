package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
// import platform.Foundation.NSBundle

@OptIn(ExperimentalResourceApi::class)
actual suspend fun fontResource(
  identifier: String,
  fontFile: String,
  weight: FontWeight,
  style: FontStyle
): Font {
//  val bundle = NSBundle.mainBundle
//  Logger.d(bundle.pathsForResourcesOfType(ext = null, inDirectory = null).joinToString())
  /* Logger.e(bundle.pathsForResourcesOfType(ext = null, inDirectory = "font").joinToString()) */
  return androidx.compose.ui.text.platform.Font(
    identity = identifier,
    data = resource(fontFile).readBytes(),
    weight = weight,
    style = style
  )
}