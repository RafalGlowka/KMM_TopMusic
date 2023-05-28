package com.glowka.rafal.topmusic.presentation.flow.intro

import app.cash.turbine.test
import com.glowka.rafal.topmusic.flow.dashboard.DashboardFlow
import com.glowka.rafal.topmusic.flow.dashboard.DashboardResult
import com.glowka.rafal.topmusic.flow.intro.IntroFlow
import com.glowka.rafal.topmusic.flow.intro.IntroFlowImpl
import com.glowka.rafal.topmusic.flow.intro.IntroResult
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.FlowSpec
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.utils.FakeFlow
import com.glowka.rafal.topmusic.presentation.utils.FakeScreenNavigator
import com.glowka.rafal.topmusic.presentation.utils.emitScreenOutput
import com.glowka.rafal.topmusic.presentation.utils.shouldBeNavigationToScreen
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class IntroFlowSpec : FlowSpec() {

  init {
    val dashboardFlow = object : DashboardFlow, FakeFlow<EmptyParam, DashboardResult>() {}

    val flow = IntroFlowImpl(
      dashboardFlow = dashboardFlow
    )

    val navigator = FakeScreenNavigator()

    it("shows intro screen after which it starts dashboard and terminates with dashboard flow") {
      var flowFinished = false
      dashboardFlow.startEvents.test {
        val dashboardStartEvents = this
        navigator.navigationEvents.test {

          flow.start(navigator, EmptyParam.EMPTY) { event ->
            when (event) {
              IntroResult.Terminated -> flowFinished = true
            }
          }

          dashboardStartEvents.expectNoEvents()
          awaitItem()
            .shouldBeNavigationToScreen(
              screen = IntroFlow.Screens.Start,
              onShowInput = null,
            )
            .emitScreenOutput(IntroViewModelToFlowInterface.Output.Finished)

          dashboardStartEvents.awaitItem().run {
            param shouldBe EmptyParam.EMPTY
            flowFinished.shouldBeFalse()
            onResult(DashboardResult.Terminated)
            flowFinished.shouldBeTrue()
            flowFinished.shouldBeFalse()
          }
        }
      }
    }
  }
}