package com.glowka.rafal.topmusic.presentation.architecture

import androidx.compose.runtime.Composable

open class ScreenFragment<
    VIEW_STATE : ViewState,
    VIEW_EVENT : ViewEvent,
    >(
  private val screenStructure: ScreenStructure<*, *, VIEW_STATE, VIEW_EVENT>
) : BaseFragment<VIEW_STATE, VIEW_EVENT>() {

  override val content: @Composable () -> Unit = {
    screenStructure.content(viewModel)
  }
}