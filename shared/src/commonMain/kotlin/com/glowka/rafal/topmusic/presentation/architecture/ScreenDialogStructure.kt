package com.glowka.rafal.topmusic.presentation.architecture

import androidx.compose.runtime.Composable
import org.koin.core.scope.Scope

enum class DialogType {
  CENTER,
  BOTTOM,
}

abstract class ScreenDialogStructure<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWSTATE : ViewState,
    VIEWEVENT : ViewEvent,
    > {
  abstract val type: DialogType
  abstract val content: @Composable (ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>) -> Unit
  abstract fun Scope.viewModelCreator(): ViewModelInterface<INPUT, OUTPUT, VIEWSTATE, VIEWEVENT>
}
