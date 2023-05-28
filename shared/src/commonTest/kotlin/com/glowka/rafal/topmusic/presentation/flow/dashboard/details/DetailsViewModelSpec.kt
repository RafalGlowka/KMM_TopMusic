package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsViewModelImpl
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import io.kotest.matchers.shouldBe

class DetailsViewModelSpec : ViewModelSpec() {

  init {

    val viewModel = DetailsViewModelImpl()
    val album = album()

    describe("initialization") {

      it("shows data from album received during initalization") {
        viewModel.onInput(DetailsViewModelToFlowInterface.Input.Init(album))

        viewModel.viewState.test {
          awaitItem().album shouldBe album
        }
      }
    }

    describe("events") {

      it("opens url when action picked") {
        viewModel.testScreenEvents {
          viewModel.onViewEvent(DetailsViewModelToViewInterface.ViewEvents.OpenURL)
          awaitItem() shouldBe DetailsViewModelToFlowInterface.Output.OpenURL(album.url)
        }
      }

      it("emits BACK event on close event") {
        viewModel.testScreenEvents {
          viewModel.onViewEvent(DetailsViewModelToViewInterface.ViewEvents.Close)
          awaitItem() shouldBe DetailsViewModelToFlowInterface.Output.Back
        }
      }

      it("emits BACK event if system back action was called") {
        viewModel.testScreenEvents {
          viewModel.onBackPressed()
          awaitItem() shouldBe DetailsViewModelToFlowInterface.Output.Back
        }
      }
    }
  }
}