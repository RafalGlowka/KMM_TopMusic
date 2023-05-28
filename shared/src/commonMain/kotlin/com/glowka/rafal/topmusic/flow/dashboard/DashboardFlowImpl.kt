package com.glowka.rafal.topmusic.flow.dashboard

// import android.content.Intent
// import android.net.Uri
import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.flow.dashboard.country.CountryDialogViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.BaseFlow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.sendInput
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam

class DashboardFlowImpl :
  BaseFlow<EmptyParam, DashboardResult>(flowScopeName = DashboardFlow.SCOPE_NAME), DashboardFlow {

  private val log = Logger.withTag("DashboardFlow")

  override fun onStart(param: EmptyParam): Screen<*, *> {
    return showScreen(
      screen = DashboardFlow.Screens.List,
      onShowInput = ListViewModelToFlowInterface.Input.Init,
      replace = true,
    ) { output ->
      when (output) {
        is ListViewModelToFlowInterface.Output.ShowDetails -> showDetails(output)
        ListViewModelToFlowInterface.Output.Back -> finish(result = DashboardResult.Terminated)
        is ListViewModelToFlowInterface.Output.ChangeCountry -> showCountryDialog(output)
      }
    }
  }

  private fun showCountryDialog(event: ListViewModelToFlowInterface.Output.ChangeCountry) {
    showScreenDialog(
      screen = DashboardFlow.ScreenDialogs.Country,
      onShowInput = CountryDialogViewModelToFlowInterface.Input.Init(selected = event.country),
      onOutput = { output ->
        when (output) {
          CountryDialogViewModelToFlowInterface.Output.Back -> hideScreenDialog(DashboardFlow.ScreenDialogs.Country)
          is CountryDialogViewModelToFlowInterface.Output.CountryPicked -> {
            hideScreenDialog(DashboardFlow.ScreenDialogs.Country)
            sendInput(
              screen = DashboardFlow.Screens.List,
              input = ListViewModelToFlowInterface.Input.SetCountry(selected = output.country)
            )
          }
        }
      }
    )
  }

  private fun showDetails(showDetails: ListViewModelToFlowInterface.Output.ShowDetails) {
    log.d("show Details")
    showScreen(
      screen = DashboardFlow.Screens.Details,
      onShowInput = DetailsViewModelToFlowInterface.Input.Init(
        album = showDetails.album,
      ),
    ) { output ->
      log.d("ShowDetails output: $output")
      when (output) {
        DetailsViewModelToFlowInterface.Output.Back -> {
          switchBackTo(DashboardFlow.Screens.List)
        }

        is DetailsViewModelToFlowInterface.Output.OpenURL -> {
          screenNavigator.openWebUrl(url = output.url)
        }
      }
    }
  }
}