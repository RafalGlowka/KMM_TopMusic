package com.glowka.rafal.topmusic.presentation.architecture

import androidx.compose.runtime.Composable
import org.koin.core.scope.Scope

interface ViewState

interface ViewEvent

abstract class ScreenStructure<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWSTATE : ViewState,
    VIEWEVENT : ViewEvent,
    > {
  abstract val content: @Composable (ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>) -> Unit
  abstract fun Scope.viewModelCreator(): ViewModelInterface<INPUT, OUTPUT, VIEWSTATE, VIEWEVENT>
}
