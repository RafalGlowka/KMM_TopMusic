package com.glowka.rafal.topmusic.flow.intro

import com.glowka.rafal.topmusic.flow.dashboard.DashboardFlow
import com.glowka.rafal.topmusic.flow.dashboard.DashboardResult
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.BaseFlow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam

class IntroFlowImpl(
  val dashboardFlow: DashboardFlow,
) :
  BaseFlow<EmptyParam, IntroResult>(flowScopeName = IntroFlow.SCOPE_NAME), IntroFlow {

  override fun onStart(param: EmptyParam): Screen<*, *> {
    showScreen(
      screen = IntroFlow.Screens.Start,
      onShowInput = null,
      onScreenOutput = ::onStartEvent
    )
    return IntroFlow.Screens.Start
  }

  private fun onStartEvent(event: IntroViewModelToFlowInterface.Output) {
    when (event) {
      IntroViewModelToFlowInterface.Output.Finished -> showDashboard()
    }
  }

  private fun showDashboard() {
//    screenNavigator.popBack(screen = IntroFlow.Screens.Start)
    dashboardFlow.start(screenNavigator = screenNavigator, param = EmptyParam.EMPTY) { result ->
      when (result) {
        DashboardResult.Terminated -> finish(result = IntroResult.Terminated)
      }
    }
  }
}
