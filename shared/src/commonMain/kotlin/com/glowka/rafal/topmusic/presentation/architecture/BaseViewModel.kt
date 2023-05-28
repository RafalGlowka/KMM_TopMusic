package com.glowka.rafal.topmusic.presentation.architecture

import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.presentation.utils.CoroutineErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface ViewModelToViewInterface<VIEWSTATE : ViewState, VIEWEVENT : ViewEvent> {
  val viewModelScope: CoroutineScope

  val viewState: StateFlow<VIEWSTATE>
  fun onViewEvent(event: VIEWEVENT)
  fun onBackPressed(): Boolean
}

interface ViewModelToFlowInterface<INPUT : ScreenInput, OUTPUT : ScreenOutput> {
  var onScreenOutput: (OUTPUT) -> Unit

  fun onInput(input: INPUT)

  fun clear()
}

interface ViewModelInterface<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWSTATE : ViewState,
    VIEWEVENT : ViewEvent
    > :
  ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>,
  ViewModelToFlowInterface<INPUT, OUTPUT>

abstract class BaseViewModel<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWSTATE : ViewState,
    VIEWEVENT : ViewEvent
    >(
  private val backPressedOutput: OUTPUT?
) : ViewModelInterface<INPUT, OUTPUT, VIEWSTATE, VIEWEVENT> {

  override lateinit var onScreenOutput: (OUTPUT) -> Unit

  //  var lifecycleOwner: LifecycleOwner? = null
  private var _viewModelScope: CloseableCoroutineScope? = null
  override val viewModelScope: CoroutineScope
    get() {
      if (_viewModelScope == null) {
        _viewModelScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
      }
      return _viewModelScope!!
    }

  override fun onInput(input: INPUT) {
    Logger.e("Function onInput(input) should be overridden")
  }

  protected fun sendOutput(output: OUTPUT) {
    onScreenOutput(output)
  }

  override fun onBackPressed(): Boolean {
    return backPressedOutput?.let { event ->
      launch {
        sendOutput(event)
      }
      true
    } ?: false
  }

  override fun clear() {
    _viewModelScope?.close()
    _viewModelScope = null
  }
}

class CloseableCoroutineScope(context: CoroutineContext) : CoroutineScope {
  override val coroutineContext: CoroutineContext = context

  fun close() {
    coroutineContext.cancel()
  }
}

// TODO : Move to DI
val coroutineErrorHandler = CoroutineErrorHandler()

fun BaseViewModel<*, *, *, *>.launch(
  context: CoroutineContext? = null,
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> Unit
): Job {
  var coroutineContext = context
  if (coroutineContext == null) {
    coroutineContext = coroutineErrorHandler
  }
  return viewModelScope.launch(coroutineContext, start, block)
}
