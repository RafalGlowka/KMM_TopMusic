package com.glowka.rafal.topmusic.di

import org.koin.core.Koin

lateinit var koinForTests: Koin

actual fun getGlobalKoin(): Koin {
  return koinForTests
}