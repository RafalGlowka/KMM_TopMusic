package com.glowka.rafal.topmusic.flow.dashboard.country

import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.model.countryName
import com.glowka.rafal.topmusic.flow.dashboard.country.CountryDialogViewModelToFlowInterface.Input
import com.glowka.rafal.topmusic.flow.dashboard.country.CountryDialogViewModelToFlowInterface.Output
import com.glowka.rafal.topmusic.flow.dashboard.country.CountryDialogViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.flow.dashboard.country.CountryDialogViewModelToViewInterface.ViewState
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import com.glowka.rafal.topmusic.presentation.architecture.ViewEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface CountryDialogViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {

  sealed interface Input : ScreenInput {
    data class Init(val selected: Country) : Input
  }

  sealed interface Output : ScreenOutput {
    data class CountryPicked(val country: Country) : Output
    object Back : Output
  }
}

interface CountryDialogViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  sealed class ViewEvents : ViewEvent {
    data class PickCountry(val position: Int) : ViewEvents()
    object Back : ViewEvents()
  }

  data class ViewState(
    val selectedIndex: Int = 0,
    val items: List<String> = emptyList(),
  ) : com.glowka.rafal.topmusic.presentation.architecture.ViewState
}

class CountryDialogViewModelImpl : CountryDialogViewModelToFlowInterface,
  CountryDialogViewModelToViewInterface,
  BaseViewModel<Input, Output, ViewState, ViewEvents>(
    backPressedOutput = Output.Back
  ) {
  override val viewState = Country.values().let { countries ->
    MutableStateFlow(
      ViewState(
        selectedIndex = countries.indexOf(Country.UnitedStates),
        items = countries.map { country ->
          country.countryName
        },
      )
    )
  }

  override fun onInput(input: Input) {
    when (input) {
      is Input.Init -> {
        viewState.update { state ->
          state.copy(selectedIndex = Country.values().indexOf(input.selected))
        }
      }
    }
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      is ViewEvents.PickCountry -> {
        sendOutput(
          output = Output.CountryPicked(
            country = Country.values()[event.position],
          )
        )
      }

      ViewEvents.Back -> {
        sendOutput(output = Output.Back)
      }
    }
  }
}
