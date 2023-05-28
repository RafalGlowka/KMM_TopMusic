package com.glowka.rafal.topmusic.flow.intro.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glowka.rafal.topmusic.MainRes
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.compose.SnackbarEvent
import com.glowka.rafal.topmusic.presentation.compose.getAppTypography
import com.glowka.rafal.topmusic.presentation.style.Colors
import com.glowka.rafal.topmusic.presentation.style.FontSize

// import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun IntroScreen(viewState: State, onEvent: (ViewEvents) -> Unit) {
  LaunchedEffect(Unit) {
    onEvent(ViewEvents.ActiveScreen)
  }

  MaterialTheme(
    typography = getAppTypography()
  ) {
//    systemUiController.setSystemBarsColor(
//      color = Color.Transparent,
//      darkIcons = useDarkIcons
//    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Colors.white)
    ) {
      Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = MainRes.string.app_name,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          fontSize = FontSize.title,
          fontWeight = FontWeight.Bold,
        )
      }

      SnackbarEvent(
        modifier = Modifier.align(Alignment.BottomCenter),
        snakbarEvent = viewState.snackbarEvent,
        onViewEvent = onEvent,
      )
    }
  }
}
