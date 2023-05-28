package com.glowka.rafal.topmusic

import android.app.Application
import com.glowka.rafal.topmusic.di.AndroidDIHelper
import com.glowka.rafal.topmusic.modules.androidModule

class TopMusicApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    AndroidDIHelper.init(this@TopMusicApplication, androidModule = androidModule)
  }
}