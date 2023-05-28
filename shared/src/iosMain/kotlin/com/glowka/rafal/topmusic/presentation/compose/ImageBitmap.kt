package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun createImageBitmap(data: ByteArray): ImageBitmap? = Image
  .makeFromEncoded(data)
  .toComposeImageBitmap()
