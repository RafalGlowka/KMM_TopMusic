@file:Suppress("Filename")

package com.glowka.rafal.topmusic

import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.di.IOSDIHelper
import com.glowka.rafal.topmusic.di.inject
import com.glowka.rafal.topmusic.flow.intro.IntroFlow
import com.glowka.rafal.topmusic.flow.intro.IntroResult
import com.glowka.rafal.topmusic.presentation.architecture.NavigationControllerNavigatorImpl
import com.glowka.rafal.topmusic.presentation.architecture.UIViewFactory
import com.glowka.rafal.topmusic.presentation.compose.initScreenData
import com.glowka.rafal.topmusic.presentation.style.initFonts
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController
import platform.posix.exit

@Suppress("FunctionNaming", "Filename")
fun InitMainViewController(viewFactory: UIViewFactory): UIViewController {
  val navigationController = UINavigationController()
  try {
    IOSDIHelper.init()
    initFonts()
    initScreenData()
    val introFlow: IntroFlow by inject()
    val navigator = NavigationControllerNavigatorImpl(navigationController, viewFactory)
    introFlow.start(screenNavigator = navigator, param = EmptyParam.EMPTY) { result ->
      when (result) {
        IntroResult.Terminated -> {
          exit(0)
        }
      }
    }
  } catch (e: Exception) {
    Logger.e("InitMainViewController", e)
  }
  return navigationController
}

fun globalLog(message: String) {
  Logger.d(message)
}
