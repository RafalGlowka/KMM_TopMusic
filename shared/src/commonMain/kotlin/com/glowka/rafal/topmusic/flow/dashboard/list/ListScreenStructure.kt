package com.glowka.rafal.topmusic.flow.dashboard.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import org.koin.core.scope.Scope

object ListScreenStructure : ScreenStructure<
    ListViewModelToFlowInterface.Input,
    ListViewModelToFlowInterface.Output,
    ListViewModelToViewInterface.ViewState,
    ListViewModelToViewInterface.ViewEvents
    >() {
  override val content =
    @Composable { viewModel: ViewModelToViewInterface<ListViewModelToViewInterface.ViewState, ListViewModelToViewInterface.ViewEvents> ->
      val viewState by viewModel.viewState.collectAsState()
      ListScreen(viewState, viewModel::onViewEvent)
    }

  override fun Scope.viewModelCreator() = ListViewModelImpl(
    musicRepository = get(),
  )
}