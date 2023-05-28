package com.glowka.rafal.topmusic.presentation.architecture

interface ScreenInput

interface ScreenOutput

abstract class Screen<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    >(
  val flowScopeName: String,
  val screenTag: String,
  val screenStructure: ScreenStructure<INPUT, OUTPUT, *, *>,
)

fun <
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    > Screen<INPUT, OUTPUT>.flowDestination(
  param: INPUT?
) = FlowDestination(
  screen = this,
  param = param
)
