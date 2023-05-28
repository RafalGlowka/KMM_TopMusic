package com.glowka.rafal.topmusic.di

import android.app.Application
import com.glowka.rafal.topmusic.presentation.style.initFonts
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object AndroidDIHelper {
  fun init(applicationContext: Application, androidModule: Module) {
    startKoin {
      androidLogger()
      androidContext(applicationContext)
      modules(commonModules)
      modules(androidModule)
      createEagerInstances()
    }

    initFonts()
  }
}

actual fun getGlobalKoin(): Koin = GlobalContext.get()