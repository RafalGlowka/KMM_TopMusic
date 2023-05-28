@file:Suppress("MaxLineLength")

package com.glowka.rafal.topmusic.presentation.utils

import com.glowka.rafal.topmusic.domain.utils.pop
import com.glowka.rafal.topmusic.domain.utils.push
import com.glowka.rafal.topmusic.domain.utils.replace
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialog
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenNavigator
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope

sealed interface NavigationEvent {
  sealed interface ScreenNavigationEvent : NavigationEvent {

    data class ReplaceScreen<
        INPUT : ScreenInput,
        OUTPUT : ScreenOutput,
        SCREEN : Screen<INPUT, OUTPUT>
        >(
      val screen: SCREEN,
      val onShowInput: INPUT?,
      val onOutput: (OUTPUT) -> Unit,
    ) : ScreenNavigationEvent

    data class PushScreen<
        INPUT : ScreenInput,
        OUTPUT : ScreenOutput,
        SCREEN : Screen<INPUT, OUTPUT>
        >(
      val screen: SCREEN,
      val onShowInput: INPUT?,
      val onOutput: (OUTPUT) -> Unit,
    ) : ScreenNavigationEvent

    data class PopBack<
        INPUT : ScreenInput,
        OUTPUT : ScreenOutput,
        SCREEN : Screen<INPUT, OUTPUT>
        >(val screen: SCREEN) : ScreenNavigationEvent

    data class PopBackTo<
        INPUT : ScreenInput,
        OUTPUT : ScreenOutput,
        SCREEN : Screen<INPUT, OUTPUT>
        >(val screen: SCREEN) : ScreenNavigationEvent
  }

  sealed interface ScreenDialogNavigationEvent : NavigationEvent {
    data class ShowScreenDialog<INPUT : ScreenInput, OUTPUT : ScreenOutput,
        SCREENDIALOG : ScreenDialog<INPUT, OUTPUT>>(

      val screenDialog: SCREENDIALOG,
      val onShowInput: INPUT?,
      val onOutput: (OUTPUT) -> Unit,
    ) : ScreenDialogNavigationEvent

    data class HideScreenDialog<INPUT : ScreenInput, OUTPUT : ScreenOutput,
        SCREENDIALOG : ScreenDialog<INPUT, OUTPUT>>(
      val screenDialog: SCREENDIALOG
    ) : ScreenDialogNavigationEvent
  }

  data class OpenWeb(val url: String) : NavigationEvent
}

class FakeScreenNavigator : ScreenNavigator {

  private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
  val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents

  val screenStack = arrayListOf<Screen<*, *>>()
  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> replace(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    screenStack.replace(screen)
    val event = NavigationEvent.ScreenNavigationEvent.ReplaceScreen(screen, onShowInput, onScreenOutput)
    MainScope().launch {
      _navigationEvents.emit(event)
    }
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> push(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    screenStack.push(screen)
    val event = NavigationEvent.ScreenNavigationEvent.PushScreen(screen, onShowInput, onScreenOutput)
    MainScope().launch {
      _navigationEvents.emit(event)
    }
  }

  override fun popBack(screen: Screen<*, *>) {
    if (screenStack.isNotEmpty()) {
      for (i in screenStack.size - 1 downTo 0) {
        if (screenStack[i] == screen) {
          for (i2 in screenStack.size - 1 downTo i)
            screenStack.pop()
          break
        }
      }
    }
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.ScreenNavigationEvent.PopBack(screen))
    }
  }

  override fun popBackTo(screen: Screen<*, *>) {
    if (screenStack.isNotEmpty()) {
      for (i in screenStack.size - 1 downTo 0) {
        if (screenStack[i] == screen) {
          for (i2 in screenStack.size - 1 downTo i + 1)
            screenStack.pop()
          break
        }
      }
    }
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.ScreenNavigationEvent.PopBackTo(screen))
    }
  }

  @Suppress("MaxLineLength")
  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> showDialog(
    scope: Scope,
    screenDialog: ScreenDialog<INPUT, OUTPUT>,
    onShowInput: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    val scd =
      NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog(screenDialog, onShowInput, onScreenOutput)
    MainScope().launch {
      _navigationEvents.emit(scd)
    }
  }

  override fun hideDialog(screenDialog: ScreenDialog<*, *>) {
    val scd = NavigationEvent.ScreenDialogNavigationEvent.HideScreenDialog(screenDialog)
    MainScope().launch {
      _navigationEvents.emit(scd)
    }
  }

  override fun openWebUrl(url: String) {
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.OpenWeb(url = url))
    }
  }
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREEN : Screen<INPUT, OUTPUT>>
    NavigationEvent.shouldBeNavigationToScreen(
  screen: SCREEN,
  onShowInput: INPUT?,
): NavigationEvent.ScreenNavigationEvent.PushScreen<INPUT, OUTPUT, SCREEN> {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.PushScreen<INPUT, OUTPUT, SCREEN>>()
    .run {
      this.screen shouldBe screen
      this.onShowInput shouldBe onShowInput
    }
  return this
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREEN : Screen<INPUT, OUTPUT>>
    NavigationEvent.ScreenNavigationEvent.PushScreen<INPUT, OUTPUT, SCREEN>.emitScreenOutput(
  output: OUTPUT
): NavigationEvent.ScreenNavigationEvent.PushScreen<INPUT, OUTPUT, SCREEN> {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.PushScreen<INPUT, OUTPUT, SCREEN>>()
    .onOutput(output)
  return this
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREEN : Screen<INPUT, OUTPUT>>
    NavigationEvent.shouldBePopBack(
  screen: SCREEN,
) {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.PopBack<INPUT, OUTPUT, SCREEN>>()
    .run {
      this.screen shouldBe screen
    }
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREEN : Screen<INPUT, OUTPUT>>
    NavigationEvent.shouldBePopBackTo(
  screen: SCREEN
) {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.PopBackTo<INPUT, OUTPUT, SCREEN>>()
    .run {
      this.screen shouldBe screen
    }
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREENDIALOG : ScreenDialog<INPUT, OUTPUT>>
    NavigationEvent.shouldBeNavigationToScreenDialog(
  screenDialog: SCREENDIALOG,
  param: INPUT,
): NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<INPUT, OUTPUT, SCREENDIALOG> {
  this.shouldBeTypeOf<NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<INPUT, OUTPUT, SCREENDIALOG>>()
    .run {
      this.screenDialog shouldBe screenDialog
      this.onShowInput shouldBe param
    }
  return this
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREENDIALOG : ScreenDialog<INPUT, OUTPUT>>
    NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<INPUT, OUTPUT, SCREENDIALOG>.emitScreenDialogOutput(
  output: OUTPUT,
): NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<INPUT, OUTPUT, SCREENDIALOG> {
  this.shouldBeTypeOf<NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<INPUT, OUTPUT, SCREENDIALOG>>()
    .onOutput(output)
  return this
}

fun <INPUT : ScreenInput, OUTPUT : ScreenOutput, SCREENDIALOG : ScreenDialog<INPUT, OUTPUT>>
    NavigationEvent.shouldBeHideScreenDialog(
  screenDialog: SCREENDIALOG,
) {
  this.shouldBeTypeOf<NavigationEvent.ScreenDialogNavigationEvent.HideScreenDialog<INPUT, OUTPUT, SCREENDIALOG>>()
    .run {
      this.screenDialog shouldBe screenDialog
    }
}

fun NavigationEvent.shouldBeOpenWeb(url : String) {
  this.shouldBeTypeOf<NavigationEvent.OpenWeb>().run {
    this.url shouldBe url
  }
}

