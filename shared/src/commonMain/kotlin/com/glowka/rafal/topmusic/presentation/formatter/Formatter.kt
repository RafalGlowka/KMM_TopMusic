package com.glowka.rafal.topmusic.presentation.formatter

interface Formatter<FROM, TO> {
  fun format(data: FROM): TO
}
