package com.glowka.rafal.topmusic.presentation.architecture

import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope

@Suppress("UNCHECKED_CAST", "UnusedParameter")
inline fun <VIEWSTATE : ViewState, VIEWEVENT : ViewEvent> ScreenNavigator.getViewModelToView(
  scope: Scope,
  screenTag: String,
  screenStructure: ScreenStructure<*, *, VIEWSTATE, VIEWEVENT>
): ViewModelToViewInterface<VIEWSTATE, VIEWEVENT> {
  val qualifier = StringQualifier(screenTag)
  return scope.get<ViewModelToViewInterface<*, *>>(qualifier = qualifier) as? ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>
    ?: throw IllegalStateException("Missing $screenTag in the scope ${scope.id}")
}

@Suppress("UNCHECKED_CAST", "UnusedParameter")
inline fun <VIEWSTATE : ViewState, VIEWEVENT : ViewEvent> ScreenNavigator.getViewModelToView(
  scope: Scope,
  screenTag: String,
  screenStructure: ScreenDialogStructure<*, *, VIEWSTATE, VIEWEVENT>
): ViewModelToViewInterface<VIEWSTATE, VIEWEVENT> {
  val qualifier = StringQualifier(screenTag)
  val viewModelToView =
    scope.get<ViewModelToViewInterface<*, *>>(qualifier = qualifier) as? ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>
      ?: throw IllegalStateException("Missing $screenTag in the scope ${scope.id}")
  return viewModelToView
}
