package com.glowka.rafal.topmusic.presentation.flow.intro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment

class AndroidIntroFragment : BaseFragment<State, ViewEvents>() {

  override val content: @Composable () -> Unit = {
    val viewState by viewModel.viewState.collectAsState()
    AndroidIntroScreen(viewState, viewModel::onViewEvent)
  }
}