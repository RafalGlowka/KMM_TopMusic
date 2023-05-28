package com.glowka.rafal.topmusic.presentation.architecture

abstract class ScreenDialog<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    >(
  val flowScopeName: String,
  val screenTag: String,
  val screenStructure: ScreenDialogStructure<INPUT, OUTPUT, *, *>,
)

fun <
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    > ScreenDialog<INPUT, OUTPUT>.flowDestination(param: INPUT?) = FlowDialogDestination(
  screen = this,
  param = param
)
