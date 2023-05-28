package com.glowka.rafal.topmusic.presentation.flow.dashboard

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.data.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.flow.dashboard.DashboardFlow
import com.glowka.rafal.topmusic.flow.dashboard.DashboardFlowImpl
import com.glowka.rafal.topmusic.flow.dashboard.DashboardResult
import com.glowka.rafal.topmusic.flow.dashboard.country.CountryDialogViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.FlowSpec
import com.glowka.rafal.topmusic.presentation.architecture.businessFlow
import com.glowka.rafal.topmusic.presentation.architecture.screen
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.utils.FakeScreenNavigator
import com.glowka.rafal.topmusic.presentation.utils.emitScreenDialogOutput
import com.glowka.rafal.topmusic.presentation.utils.emitScreenOutput
import com.glowka.rafal.topmusic.presentation.utils.shouldBeHideScreenDialog
import com.glowka.rafal.topmusic.presentation.utils.shouldBeNavigationToScreen
import com.glowka.rafal.topmusic.presentation.utils.shouldBeNavigationToScreenDialog
import com.glowka.rafal.topmusic.presentation.utils.shouldBeOpenWeb
import com.glowka.rafal.topmusic.presentation.utils.shouldBePopBackTo
import io.kotest.matchers.booleans.shouldBeTrue
import org.koin.core.module.Module

class DashboardFlowSpec : FlowSpec() {

  init {

    fun createFlow() = DashboardFlowImpl()
    val navigator = FakeScreenNavigator()

    it("starts with list screen and terminated on list screen event back") {
      val flow = createFlow()
      var flowTerminated = false
      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) { result ->
          when (result) {
            DashboardResult.Terminated -> flowTerminated = true
          }
        }
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          onShowInput = ListViewModelToFlowInterface.Input.Init,
        ).emitScreenOutput(ListViewModelToFlowInterface.Output.Back)
        flowTerminated.shouldBeTrue()
      }
    }

    it("Shows details screen if list screen emit ShowDetails event") {
      val album = album()
      val flow = createFlow()

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          onShowInput = ListViewModelToFlowInterface.Input.Init,
        ).emitScreenOutput(ListViewModelToFlowInterface.Output.ShowDetails(album))

        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.Details,
          onShowInput = DetailsViewModelToFlowInterface.Input.Init(album = album)
        ).emitScreenOutput(DetailsViewModelToFlowInterface.Output.Back)

        awaitItem().shouldBePopBackTo(screen = DashboardFlow.Screens.List)
      }
    }

    it("Opens system browser if visit button was pressed on list screen") {
      val album = album()
      val flow = createFlow()
      val url = "www.wp.pl/test"

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          onShowInput = ListViewModelToFlowInterface.Input.Init,
        ).emitScreenOutput(ListViewModelToFlowInterface.Output.ShowDetails(album))

        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.Details,
          onShowInput = DetailsViewModelToFlowInterface.Input.Init(album = album)
        ).emitScreenOutput(DetailsViewModelToFlowInterface.Output.OpenURL(url))

        awaitItem().shouldBeOpenWeb(url = url)
      }
    }

    it("Shows country picker if Pick country was clicked on list screen") {
      val flow = createFlow()
      val country = Country.Angola

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          onShowInput = ListViewModelToFlowInterface.Input.Init,
        ).emitScreenOutput(ListViewModelToFlowInterface.Output.ChangeCountry(country))

        awaitItem().shouldBeNavigationToScreenDialog(
          screenDialog = DashboardFlow.ScreenDialogs.Country,
          param = CountryDialogViewModelToFlowInterface.Input.Init(selected = country)
        )
          .emitScreenDialogOutput(CountryDialogViewModelToFlowInterface.Output.CountryPicked(Country.Poland))

        awaitItem().shouldBeHideScreenDialog(
          screenDialog = DashboardFlow.ScreenDialogs.Country
        )
      }
    }
  }

  override fun Module.prepareKoinContext() {
    single<MusicRepository> {
      FakeMusicRepository()
    }

    businessFlow(DashboardFlow.SCOPE_NAME) {
      screen(DashboardFlow.Screens.List)
    }
  }
}