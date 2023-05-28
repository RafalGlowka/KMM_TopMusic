package com.glowka.rafal.topmusic.presentation.architecture

import org.koin.core.definition.Definition
import org.koin.core.qualifier.named
import org.koin.dsl.ScopeDSL
import org.koin.dsl.binds

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScopeDSL.screenViewModel(
  screen: Screen<INPUT, OUTPUT>,
  definition: Definition<ViewModelInterface<INPUT, OUTPUT, *, *>>
) {
  scoped(
    qualifier = named(screen.screenTag),
    definition = definition,
  ) binds arrayOf(ViewModelToFlowInterface::class, BaseViewModel::class, ViewModelToViewInterface::class)
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScopeDSL.screen(
  screen: Screen<INPUT, OUTPUT>
) {
  scoped(
    qualifier = named(screen.screenTag),
    definition = {
      with(screen.screenStructure) {
        viewModelCreator()
      }
    }
  ) binds arrayOf(ViewModelToFlowInterface::class, BaseViewModel::class, ViewModelToViewInterface::class)
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScopeDSL.screenDialog(
  screen: ScreenDialog<INPUT, OUTPUT>
) {
  scoped(
    qualifier = named(screen.screenTag),
    definition = {
      with(screen.screenStructure) {
        viewModelCreator()
      }
    }
  ) binds arrayOf(ViewModelToFlowInterface::class, BaseViewModel::class, ViewModelToViewInterface::class)
}
