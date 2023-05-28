package com.glowka.rafal.topmusic.presentation.architecture

import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope

interface ScreenNavigator {
  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > replace(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  )

  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > push(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  )

  fun popBack(screen: Screen<*, *>)
  fun popBackTo(screen: Screen<*, *>)
  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > showDialog(
    scope: Scope,
    screenDialog: ScreenDialog<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  )

  fun hideDialog(screenDialog: ScreenDialog<*, *>)
  fun openWebUrl(url: String)
}

@Suppress("UNCHECKED_CAST")
fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScreenNavigator.initFlowDestination(
  scope: Scope,
  flowDestination: FlowDestination<INPUT, OUTPUT>,
  onScreenOUTPUT: (OUTPUT) -> Unit,
) {
  val qualifier = StringQualifier(flowDestination.screen.screenTag)
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? ViewModelToFlowInterface<INPUT, OUTPUT>
      ?: throw IllegalStateException("Missing ${flowDestination.screen.screenTag} in the scope ${scope.id}")
  flowDestination.param?.let { input ->
    viewModelToFlow.onInput(input = input)
  }
  viewModelToFlow.onScreenOutput = onScreenOUTPUT
}

@Suppress("UNCHECKED_CAST")
fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScreenNavigator.initFlowDestination(
  scope: Scope,
  flowDestination: FlowDialogDestination<INPUT, OUTPUT>,
  onScreenOUTPUT: (OUTPUT) -> Unit,
) {
  val qualifier = StringQualifier(flowDestination.screen.screenTag)
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? ViewModelToFlowInterface<INPUT, OUTPUT>
      ?: throw IllegalStateException("Missing ${flowDestination.screen.screenTag} in the scope ${scope.id}")
  flowDestination.param?.let { input ->
    viewModelToFlow.onInput(input = input)
  }
  viewModelToFlow.onScreenOutput = onScreenOUTPUT
}
