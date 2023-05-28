package com.glowka.rafal.topmusic.flow.intro

import com.glowka.rafal.topmusic.flow.intro.intro.IntroScreenStructure
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.architecture.IOSScreenConnector
import com.glowka.rafal.topmusic.presentation.compose.isTriggered
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class IntroScreenConnectorFactory {
  fun getScreenStructure() = IntroScreenStructure
  fun getConnector() = IntroScreenConnector()
}

class IntroScreenConnector : IOSScreenConnector<State, ViewEvents>(IntroFlow.Screens.Start) {

  var onSnackbarShow: ((String, String?) -> Unit)? = null
  private var onSnackbarActionEvent: ViewEvents? = null

  init {

    viewModel.viewState.onEach { state ->
      state.snackbarEvent.isTriggered { data ->
        onSnackbarShow?.let { action ->
          onSnackbarActionEvent = data.actionEvent
          action(data.message, data.actionLabel)
          viewModel.onViewEvent(data.onShowEvent)
        }
      }
    }.launchIn(viewModel.viewModelScope)
  }

  fun onShow() {
    viewModel.onViewEvent(event = ViewEvents.ActiveScreen)
  }

  fun onSnackbarAction() {
    onSnackbarActionEvent?.let { event ->
      viewModel.onViewEvent(event = event)
    }
  }
}
