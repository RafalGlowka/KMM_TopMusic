package com.glowka.rafal.topmusic.flow.dashboard.country

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.DialogType
import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialogStructure
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import org.koin.core.scope.Scope

object CountryScreenDialogStructure : ScreenDialogStructure<
   CountryDialogViewModelToFlowInterface.Input,
   CountryDialogViewModelToFlowInterface.Output,
   CountryDialogViewModelToViewInterface.ViewState,
   CountryDialogViewModelToViewInterface.ViewEvents
   >() {

  override val type = DialogType.BOTTOM

  override val content =
    @Composable { viewModel: ViewModelToViewInterface<
       CountryDialogViewModelToViewInterface.ViewState,
       CountryDialogViewModelToViewInterface.ViewEvents
       > ->
      val viewState by viewModel.viewState.collectAsState()
      CountryScreenDialog(viewState, viewModel::onViewEvent)
    }

  override fun Scope.viewModelCreator() = CountryDialogViewModelImpl()
}