package com.glowka.rafal.topmusic.di

import org.koin.core.Koin
import org.koin.core.context.startKoin

private lateinit var globalKoin: Koin

object IOSDIHelper {
  fun init() {
    globalKoin = startKoin {
      modules(commonModules)
      createEagerInstances()
    }.koin
  }
}

actual fun getGlobalKoin(): Koin = globalKoin
