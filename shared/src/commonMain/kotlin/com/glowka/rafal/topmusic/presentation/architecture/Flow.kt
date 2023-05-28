package com.glowka.rafal.topmusic.presentation.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.qualifier.StringQualifier
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

data class FlowDestination<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    >(
  val screen: Screen<INPUT, OUTPUT>,
  val param: INPUT?
)

data class FlowDialogDestination<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    >(
  val screen: ScreenDialog<INPUT, OUTPUT>,
  val param: INPUT?
)

interface Flow<FLOW_PARAM, FLOW_RESULT : Any> {
  val flowScopeName: String
  fun start(screenNavigator: ScreenNavigator, param: FLOW_PARAM, onResult: (FLOW_RESULT) -> Unit)
  fun finish(result: FLOW_RESULT)
}

abstract class BaseFlow<FLOW_PARAM, FLOW_RESULT : Any>(override val flowScopeName: String) :
  Flow<FLOW_PARAM, FLOW_RESULT> {

  protected lateinit var screenNavigator: ScreenNavigator
  private lateinit var onResult: (FLOW_RESULT) -> Unit

  private var _flowScope: CloseableCoroutineScope? = null
  val flowScope: CoroutineScope
    get() {
      if (_flowScope == null) {
        _flowScope =
          CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
      }
      return _flowScope!!
    }

  private var firstScreen: Screen<*, *>? = null

  abstract fun onStart(param: FLOW_PARAM): Screen<*, *>

  override fun start(
    screenNavigator: ScreenNavigator,
    param: FLOW_PARAM,
    onResult: (FLOW_RESULT) -> Unit
  ) {
    this.screenNavigator = screenNavigator
    this.onResult = onResult
    firstScreen = onStart(param = param)
  }

  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > showScreen(
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    replace: Boolean = false,
    onScreenOutput: (OUTPUT) -> Unit,
  ): Screen<INPUT, OUTPUT> {
    if (replace) {
      screenNavigator.replace(createScope(), screen, onShowInput, onScreenOutput)
    } else {
      screenNavigator.push(createScope(), screen, onShowInput, onScreenOutput)
    }
    return screen
  }

  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > showScreenDialog(
    screen: ScreenDialog<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onOutput: (OUTPUT) -> Unit
  ): ScreenDialog<INPUT, OUTPUT> {
    screenNavigator.showDialog(createScope(), screen, onShowInput, onOutput)
    return screen
  }

  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > hideScreenDialog(
    screen: ScreenDialog<INPUT, OUTPUT>,
  ) {
    screenNavigator.hideDialog(screen)
  }

  fun switchBackTo(screen: Screen<*, *>) {
    screenNavigator.popBackTo(screen = screen)
  }

  override fun finish(result: FLOW_RESULT) {
    onResult(result)
    _flowScope?.close()
    closeScope()
  }
}

@Suppress("UNCHECKED_CAST")
fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> BaseFlow<*, *>.sendInput(
  screen: Screen<INPUT, OUTPUT>,
  input: INPUT,
) {
  val qualifier = StringQualifier(screen.screenTag)
  val scope = createScope()
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? ViewModelToFlowInterface<INPUT, OUTPUT>
      ?: throw IllegalStateException("Missing ${screen.screenTag} in the scope $flowScopeName")
  viewModelToFlow.onInput(input)
}

@Suppress("UNCHECKED_CAST")
fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> BaseFlow<*, *>.sendInput(
  screenDialog: ScreenDialog<INPUT, OUTPUT>,
  input: INPUT,
) {
  val qualifier = StringQualifier(screenDialog.screenTag)
  val scope = createScope()
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? ViewModelToFlowInterface<INPUT, OUTPUT>
      ?: throw IllegalStateException("Missing ${screenDialog.screenTag} in the scope $flowScopeName")
  viewModelToFlow.onInput(input)
}

fun BaseFlow<*, *>.launch(
  context: CoroutineContext = EmptyCoroutineContext,
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> Unit
): Job = flowScope.launch(context, start, block)
