package com.glowka.rafal.topmusic.presentation.architecture

import androidx.compose.runtime.Composable

class ScreenDialogCenterFragment<
    VIEW_STATE : ViewState,
    VIEW_EVENT : ViewEvent,
    VIEW_MODEL : ViewModelToViewInterface<VIEW_STATE, VIEW_EVENT>
    >(
  val screenStructure: ScreenDialogStructure<*, *, VIEW_STATE, VIEW_EVENT>
) : BaseDialogFragment<VIEW_STATE, VIEW_EVENT, VIEW_MODEL>() {

  override val content: @Composable () -> Unit = {
    screenStructure.content(viewModel)
  }
}