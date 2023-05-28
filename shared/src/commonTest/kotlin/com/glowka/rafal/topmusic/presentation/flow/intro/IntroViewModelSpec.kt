package com.glowka.rafal.topmusic.presentation.flow.intro

import androidx.compose.material.SnackbarDuration
import app.cash.turbine.test
import com.glowka.rafal.topmusic.data.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelImpl
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import io.kotest.matchers.shouldBe
import com.glowka.rafal.topmusic.presentation.compose.SnackbarEvent
import com.glowka.rafal.topmusic.presentation.compose.ViewStateEvent
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.utils.io.errors.IOException

class IntroViewModelSpec : ViewModelSpec() {

  init {
    val musicRepository = FakeMusicRepository()
    fun createViewModel(): IntroViewModelImpl {
      val viewModel = IntroViewModelImpl(
        musicRepository = musicRepository,
      )
      return viewModel
    }

    describe("screen activation") {

      it("do not call backend, but waits 4 seconds if list was initialized from local storage") {
        val viewModel = createViewModel()
        viewModel.testScreenEvents {
          musicRepository.setInitResponse(Result.success(true))
          musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          expectNoEvents()
          advanceTimeBy(3000)
          expectNoEvents()
          advanceTimeBy(1100)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Output.Finished
        }
      }

      it("call backend if not initialized from local storage") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.success(listOf(album())), 10000)
        viewModel.testScreenEvents {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          advanceTimeBy(5000)
          expectNoEvents()
          advanceTimeBy(6000)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Output.Finished
        }
      }

      it("shows error if initialization and calling be fails") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.failure(IllegalStateException("initialization error")))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }

          }
        }
      }

      it("call repository again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.failure(IllegalStateException("initialization error")))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }

          musicRepository.setInitResponse(Result.success(true))
          musicRepository.initializing.test {
            viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.SnackbarRetry)
            awaitItem()
          }

          expectNoEvents()
        }
      }

      it("shows error if getting data from BE fails") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }
        }

      }

      it("call backend again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }

          musicRepository.setReloadResponse(Result.success(listOf(album())))
          musicRepository.reloading.test {
            viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.SnackbarRetry)
            awaitItem()
          }

          expectNoEvents()
        }
      }

      it("shows error with retry if response from backend is empty") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.success(emptyList()))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "Something went wrong."
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }

          musicRepository.setReloadResponse(Result.success(listOf(album())))
          musicRepository.reloading.test {
            viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.SnackbarRetry)
            awaitItem()
          }

          expectNoEvents()
        }
      }
    }
  }

}