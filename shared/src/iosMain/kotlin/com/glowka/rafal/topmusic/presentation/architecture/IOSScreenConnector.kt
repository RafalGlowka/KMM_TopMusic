package com.glowka.rafal.topmusic.presentation.architecture

import com.glowka.rafal.topmusic.di.getGlobalKoin
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

open class IOSScreenConnector<VIEWSTATE : ViewState, VIEWEVENT : ViewEvent>(val screen: Screen<*, *>) {
  val scope = getGlobalKoin().getOrCreateScope(screen.flowScopeName, named(screen.flowScopeName))
  val viewModel = getViewModelToView(scope, screen.screenTag)
}

@Suppress("UNCHECKED_CAST")
inline fun <VIEWSTATE : ViewState, VIEWEVENT : ViewEvent> IOSScreenConnector<VIEWSTATE, VIEWEVENT>.getViewModelToView(
  scope: Scope,
  screenTag: String,
): ViewModelToViewInterface<VIEWSTATE, VIEWEVENT> {
  val qualifier = StringQualifier(screenTag)
  val viewModelToView =
    scope.get<ViewModelToViewInterface<*, *>>(qualifier = qualifier) as? ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>
      ?: throw IllegalStateException("Missing $screenTag in the scope ${scope.id}")
  return viewModelToView
}
