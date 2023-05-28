package com.glowka.rafal.topmusic.presentation.compose

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap

actual fun createImageBitmap(data: ByteArray) = BitmapFactory
  .decodeByteArray(data, 0, data.size)
  ?.asImageBitmap()