package com.glowka.rafal.topmusic.presentation.style

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.glowka.rafal.topmusic.presentation.compose.fontResource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun initFonts() {
  GlobalScope.launch {
    loadTypography()
  }
}

internal suspend fun loadTypography(): Typography = coroutineScope {
  val regular = async { fontResource("regular", "regular.otf", FontWeight.Normal, FontStyle.Normal) }
  val medium = async { fontResource("medium", "medium.otf", FontWeight.Medium, FontStyle.Normal) }
  val bold = async { fontResource("bold", "bold.otf", FontWeight.Bold, FontStyle.Normal) }
  val semiBold = async { fontResource("semi-bold", "semibold_italic.otf", FontWeight.Bold, FontStyle.Normal) }
  Typography(
    defaultFontFamily = FontFamily(awaitAll(regular, medium, bold, semiBold))
  )
}

object FontSize {
  val small: TextUnit = 12.sp
  val base: TextUnit = 16.sp
  val big: TextUnit = 18.sp
  val title: TextUnit = 34.sp
}
