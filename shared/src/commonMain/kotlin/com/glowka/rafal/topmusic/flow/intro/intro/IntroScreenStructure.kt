package com.glowka.rafal.topmusic.flow.intro.intro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import org.koin.core.scope.Scope

object IntroScreenStructure : ScreenStructure<
    IntroViewModelToFlowInterface.Input,
    IntroViewModelToFlowInterface.Output,
    IntroViewModelToViewInterface.State,
    IntroViewModelToViewInterface.ViewEvents
    >() {

  override val content =
    @Composable { viewModel: ViewModelToViewInterface<IntroViewModelToViewInterface.State, IntroViewModelToViewInterface.ViewEvents> ->
      val viewState by viewModel.viewState.collectAsState()
      IntroScreen(viewState, viewModel::onViewEvent)
    }

  override fun Scope.viewModelCreator() = IntroViewModelImpl(
    musicRepository = get(),
  )
}
