package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import androidx.compose.material.SnackbarDuration
import app.cash.turbine.test
import com.glowka.rafal.topmusic.data.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelImpl
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.compose.SnackbarEvent
import com.glowka.rafal.topmusic.presentation.compose.ViewStateEvent
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.ktor.utils.io.errors.IOException

class ListViewModelSpec : ViewModelSpec() {

  init {
    val musicRepository = FakeMusicRepository()
    fun createViewModel() = ListViewModelImpl(
      musicRepository = musicRepository,
    )

    var viewModel = createViewModel()

    describe("initialization") {

      it("start observing album data when init is called") {
        viewModel.viewState.test {
          awaitItem().items.shouldBeEmpty()

          val album = album()
          musicRepository.setReloadResponse(Result.success(listOf(album)))
          musicRepository.reloadFromBackend()
          expectNoEvents()

          viewModel.onInput(ListViewModelToFlowInterface.Input.Init)

          awaitItem().items shouldBe listOf(album)
        }
      }

      it("start observing country data when init is called") {
        viewModel = createViewModel()
        viewModel.viewState.test {
          awaitItem().country shouldBe Country.UnitedStates
          musicRepository.setChangeCountryResponse(Result.success(true))
          musicRepository.changeCountryWithLocalStorage(Country.Poland)
          expectNoEvents()

          viewModel.onInput(ListViewModelToFlowInterface.Input.Init)
          awaitItem() // first update is after subscription of albums
          awaitItem().country shouldBe Country.Poland
        }
      }
    }

    val album = album(id = "1234565")
    val album2 = album(id = "4321", name = "Manna z nieba")

    it("refreshes view state when albums are changed in repository") {
      viewModel.viewState.test {
        awaitItem()

        musicRepository.setReloadResponse(Result.success(listOf(album, album2)))

        musicRepository.reloadFromBackend()

        awaitItem().items shouldBe listOf(album, album2)
      }

    }

    describe("View events") {

      it("refresh list state on refresh event") {
        val album3 = album("album3")
        musicRepository.setReloadResponse(Result.success(listOf(album3)))

        viewModel.viewState.test {
          awaitItem().items shouldBe listOf(album, album2)

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)

          awaitItem().items shouldBe listOf(album3)
        }
      }

      it("shows refreshing symbol during refresh action") {
        viewModel.viewState.test {
          awaitItem().isRefreshing.shouldBeFalse()
          val album4 = album()
          musicRepository.setReloadResponse(Result.success(listOf(album4)), 1000)

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)
          awaitItem().isRefreshing.shouldBeTrue()
          advanceTimeBy(2000)
          awaitItem().isRefreshing.shouldBeFalse()
        }
      }

      it("emits ShowDetails event when album is picked") {
        val album4 = album(id = "4")
        viewModel.testScreenEvents {
          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.PickedAlbum(album4))
          awaitItem() shouldBe ListViewModelToFlowInterface.Output.ShowDetails(album4)
        }
      }

      it("emits Back event when system back action is called") {
        viewModel.testScreenEvents {
          viewModel.onBackPressed()
          awaitItem() shouldBe ListViewModelToFlowInterface.Output.Back
        }
      }

      it("emits pick country when pick country action was clicked") {
        viewModel.testScreenEvents {
          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.PickCountry)
          awaitItem() shouldBe ListViewModelToFlowInterface.Output.ChangeCountry(
            country = viewModel.viewState.value.country
          )
        }
      }
    }

    describe("Changing country") {
      it("do not call backend, if albums exists in local storage") {
        viewModel.viewState.test {
          awaitItem().country shouldBe Country.Poland

          musicRepository.setChangeCountryResponse(Result.success(true))
          musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

          viewModel.onInput(ListViewModelToFlowInterface.Input.SetCountry(Country.UnitedKingdom))
          awaitItem().run {
            country shouldBe Country.UnitedKingdom
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Idle<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
          }
        }
      }

      it("call backend if not exist in local storage") {
        viewModel.viewState.test {
          awaitItem().country shouldBe Country.UnitedKingdom

          musicRepository.setChangeCountryResponse(Result.success(false))
          musicRepository.setReloadResponse(Result.success(listOf(album(id = "1234"))))
          viewModel.onInput(ListViewModelToFlowInterface.Input.SetCountry(Country.Germany))
          awaitItem().run {
            country shouldBe Country.Germany
            items shouldBe listOf(album(id = "1234"))
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Idle<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
          }
        }
      }

      it("shows error if failing both - getting data from local storage and backend") {
        musicRepository.setChangeCountryResponse(Result.failure(IllegalStateException("initialization error")))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onInput(ListViewModelToFlowInterface.Input.SetCountry(Country.UnitedStates))
          awaitItem().run {
            country shouldBe Country.UnitedStates
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
              .data
              .run {
                message shouldBe "connection error"
                duration shouldBe SnackbarDuration.Long
                actionLabel.shouldBeNull()
              }
          }
        }
      }

      it("shows error if getting data from backend fails") {
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onInput(ListViewModelToFlowInterface.Input.SetCountry(Country.Poland))
          awaitItem().run {
            country shouldBe Country.Poland
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
              .data
              .run {
                message shouldBe "connection error"
                duration shouldBe SnackbarDuration.Long
                actionLabel.shouldBeNull()
              }
          }
        }
      }
    }

    describe("issues reporting") {
      it("show error message in case of connection problems during refresh") {
        viewModel.viewState.test {
          awaitItem().snackbarEvent.shouldBeTypeOf<ViewStateEvent.Idle<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
          musicRepository.setReloadResponse(Result.failure(IOException("Connection problem")))

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)

          awaitItem().snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
            .data
            .run {
              message shouldBe "Connection problem"
            }

        }
      }
      it("shows error message in case od connection problems during country change") {
        viewModel.viewState.test {
          awaitItem().snackbarEvent.shouldBeTypeOf<ViewStateEvent.Idle<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()

          musicRepository.setChangeCountryResponse(Result.success(false))
          musicRepository.setReloadResponse(Result.failure(IOException("Connection problem")))

          viewModel.onInput(ListViewModelToFlowInterface.Input.SetCountry(Country.Germany))

          awaitItem().snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<ListViewModelToViewInterface.ViewEvents>>>()
            .data
            .run {
              message shouldBe "Connection problem"
            }
        }
      }
    }


  }
}