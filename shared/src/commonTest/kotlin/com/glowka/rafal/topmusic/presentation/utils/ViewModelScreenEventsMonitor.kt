package com.glowka.rafal.topmusic.presentation.utils

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ViewModelScreenEventsMonitor<OUTPUT : ScreenOutput>(viewModel: BaseViewModel<*, OUTPUT, *, *>) {
  private val _outputs = MutableSharedFlow<OUTPUT>()
  val outputs: SharedFlow<OUTPUT> = _outputs

  init {
    viewModel.onScreenOutput = { event ->
      MainScope().launch {
        _outputs.emit(event)
      }
    }
  }
}

suspend fun <OUTPUT : ScreenOutput> BaseViewModel<*, OUTPUT, *, *>.testScreenEvents(
  validate: suspend ReceiveTurbine<OUTPUT>.() -> Unit
) {
  ViewModelScreenEventsMonitor(this).outputs.test(validate = validate)
}
