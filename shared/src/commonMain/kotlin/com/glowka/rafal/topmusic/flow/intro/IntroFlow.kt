package com.glowka.rafal.topmusic.flow.intro

import com.glowka.rafal.topmusic.flow.intro.intro.IntroScreenStructure
import com.glowka.rafal.topmusic.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.Flow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam

sealed class IntroResult {
  object Terminated : IntroResult()
}

@Suppress("MaxLineLength")
interface IntroFlow : Flow<EmptyParam, IntroResult> {

  companion object {
    const val SCOPE_NAME = "Intro"
  }

  sealed class Screens<INPUT : ScreenInput, OUTPUT : ScreenOutput>(
    screenTag: String,
    screenStructure: ScreenStructure<INPUT, OUTPUT, *, *>
  ) :
    Screen<INPUT, OUTPUT>(
      flowScopeName = SCOPE_NAME,
      screenTag = screenTag,
      screenStructure = screenStructure
    ) {
    object Start :
      Screens<IntroViewModelToFlowInterface.Input, IntroViewModelToFlowInterface.Output>(
        screenTag = "Start",
        screenStructure = IntroScreenStructure,
      )
  }
}
