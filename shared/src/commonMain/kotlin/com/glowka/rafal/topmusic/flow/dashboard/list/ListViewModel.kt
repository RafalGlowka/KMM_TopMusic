package com.glowka.rafal.topmusic.flow.dashboard.list

import androidx.compose.material.SnackbarDuration
import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.MainRes
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EMPTY
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToFlowInterface.Input
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToFlowInterface.Output
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToViewInterface.ViewState
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import com.glowka.rafal.topmusic.presentation.architecture.ViewEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.compose.SnackbarEvent
import com.glowka.rafal.topmusic.presentation.compose.ViewStateEvent
import com.glowka.rafal.topmusic.presentation.compose.idle
import com.glowka.rafal.topmusic.presentation.compose.trigger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ListViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {

  sealed interface Input : ScreenInput {
    object Init : Input

    data class SetCountry(val selected: Country) : Input

    object Refresh : Input
  }

  sealed interface Output : ScreenOutput {
    data class ShowDetails(val album: Album) : Output
    data class ChangeCountry(val country: Country) : Output
    object Back : Output
  }
}

interface ListViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  sealed class ViewEvents : ViewEvent {
    data class PickedAlbum(val album: Album) : ViewEvents()
    object RefreshList : ViewEvents()
    object PickCountry : ViewEvents()
    object SnackbarShown : ViewEvents()
  }

  data class ViewState(
    val errorMessage: String = String.EMPTY,
    val snackbarEvent: ViewStateEvent<SnackbarEvent<ViewEvents>> = idle(),
    val isRefreshing: Boolean = false,
    val country: Country = Country.UnitedStates,
    val items: List<Album> = emptyList()
  ) : com.glowka.rafal.topmusic.presentation.architecture.ViewState
}

class ListViewModelImpl(
  private val musicRepository: MusicRepository,
) : ListViewModelToViewInterface, ListViewModelToFlowInterface, BaseViewModel<Input, Output, ViewState, ViewEvents>(
  backPressedOutput = Output.Back
) {
  private val log = Logger.withTag("ListViewModel")
  override val viewState = MutableStateFlow(ViewState())

  override fun onInput(input: Input) {
    when (input) {
      Input.Init -> init()
      is Input.SetCountry -> setCountry(input.selected)
      Input.Refresh -> refresh()
    }
  }

  private fun init() {
    musicRepository.albums.onEach { albums -> updateList(albums) }.launchIn(viewModelScope)
    musicRepository.country.onEach { country ->
        viewState.update { state -> state.copy(country = country) }
      }.launchIn(viewModelScope)
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      is ViewEvents.PickedAlbum -> {
        sendOutput(
          output = Output.ShowDetails(
            album = event.album,
          )
        )
      }

      ViewEvents.RefreshList -> {
        refresh()
      }

      ViewEvents.PickCountry -> {
        sendOutput(output = Output.ChangeCountry(country = viewState.value.country))
      }

      ViewEvents.SnackbarShown -> {
        viewState.update { state -> state.copy(snackbarEvent = idle()) }
      }
    }
  }

  private fun updateList(albums: List<Album>) {
    log.d("updateList ${albums.size}")
    if (albums.isEmpty()) {
      val errorMessage = MainRes.string.list_is_empty
      viewState.update { state ->
        state.copy(
          items = emptyList(),
          errorMessage = errorMessage,
          isRefreshing = false,
        )
      }
    } else {
      viewState.update { state ->
        state.copy(errorMessage = String.EMPTY, items = albums, isRefreshing = false)
      }
    }
  }

  private fun refresh() {
    launch {
      viewState.update { state -> state.copy(isRefreshing = true) }
      musicRepository.reloadFromBackend().onFailure { error ->
          showError(error.message ?: "Connection problem")
        }
      viewState.update { state -> state.copy(isRefreshing = false) }
    }
  }

  private fun setCountry(country: Country) {
    viewModelScope.launch {
      viewState.update { state -> state.copy(isRefreshing = true) }
      musicRepository.changeCountryWithLocalStorage(country).recover { false }.onSuccess { result ->
          if (!result) {
            musicRepository.reloadFromBackend().onFailure { error ->
                showError(error.message ?: "Connection problem")
              }
          }
        }
      viewState.update { state -> state.copy(isRefreshing = false) }
    }
  }

  private fun showError(message: String) {
    viewState.update { state ->
      state.copy(
        snackbarEvent = trigger(
          SnackbarEvent(message = message, onShowEvent = ViewEvents.SnackbarShown, duration = SnackbarDuration.Long)
        )
      )
    }
  }
}
