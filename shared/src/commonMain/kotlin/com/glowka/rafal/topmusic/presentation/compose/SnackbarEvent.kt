package com.glowka.rafal.topmusic.presentation.compose

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.presentation.architecture.ViewEvent

data class SnackbarEvent<EVENT : ViewEvent>(
  val message: String,
  val onShowEvent: EVENT,
  val duration: SnackbarDuration = SnackbarDuration.Long,
  val actionLabel: String? = null,
  val actionEvent: EVENT? = null,
)

@Composable
fun <EVENT : ViewEvent> SnackbarEvent(
  snakbarEvent: ViewStateEvent<SnackbarEvent<EVENT>>,
  onViewEvent: (EVENT) -> Unit,
  modifier: Modifier = Modifier,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  ViewStateEventCheck(
    event = snakbarEvent,
    onConsume = { event -> onViewEvent(event.onShowEvent) }
  ) { event ->
    Logger.d("Showing snackbar")
    when (
      snackbarHostState.showSnackbar(
        message = event.message,
        actionLabel = event.actionEvent?.let { event.actionLabel },
        duration = event.duration,
      )
    ) {
      SnackbarResult.ActionPerformed -> {
        event.actionEvent?.let { actionEvent ->
          onViewEvent(actionEvent)
        }
      }

      SnackbarResult.Dismissed -> {
        // nop
      }
    }
  }

  SnackbarHost(
    modifier = modifier,
    hostState = snackbarHostState,
  )
}
