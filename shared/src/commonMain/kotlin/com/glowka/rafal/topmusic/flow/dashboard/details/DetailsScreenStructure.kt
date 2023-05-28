package com.glowka.rafal.topmusic.flow.dashboard.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import org.koin.core.scope.Scope

object DetailsScreenStructure : ScreenStructure<
   DetailsViewModelToFlowInterface.Input,
   DetailsViewModelToFlowInterface.Output,
   DetailsViewModelToViewInterface.ViewState,
   DetailsViewModelToViewInterface.ViewEvents
   >() {
  override val content =
    @Composable { viewModel: ViewModelToViewInterface<
       DetailsViewModelToViewInterface.ViewState,
       DetailsViewModelToViewInterface.ViewEvents
       > ->
      val viewState by viewModel.viewState.collectAsState()
      DetailsScreen(viewState, viewModel::onViewEvent)
    }

  override fun Scope.viewModelCreator() = DetailsViewModelImpl()
}