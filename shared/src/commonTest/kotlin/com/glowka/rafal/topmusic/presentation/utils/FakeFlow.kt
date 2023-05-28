package com.glowka.rafal.topmusic.presentation.utils

import com.glowka.rafal.topmusic.presentation.architecture.Flow
import com.glowka.rafal.topmusic.presentation.architecture.ScreenNavigator
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

abstract class FakeFlow<FLOW_PARAM, FLOW_RESULT : Any> : Flow<FLOW_PARAM, FLOW_RESULT> {
  override val flowScopeName: String = "FakeFlow"

  inner class FlowStartEvent(val param: FLOW_PARAM, val onResult: (FLOW_RESULT) -> Unit)

  private val _startEvents = MutableSharedFlow<FlowStartEvent>()
  val startEvents: SharedFlow<FlowStartEvent> = _startEvents

  override fun start(
    screenNavigator: ScreenNavigator,
    param: FLOW_PARAM,
    onResult: (FLOW_RESULT) -> Unit
  ) {
    MainScope().launch {
      _startEvents.emit(FlowStartEvent(param = param, onResult = onResult))
    }
  }

  override fun finish(result: FLOW_RESULT) {}
}