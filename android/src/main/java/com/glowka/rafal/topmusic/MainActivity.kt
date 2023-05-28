package com.glowka.rafal.topmusic

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.glowka.rafal.topmusic.di.inject
import com.glowka.rafal.topmusic.flow.intro.IntroFlow
import com.glowka.rafal.topmusic.flow.intro.IntroResult
import com.glowka.rafal.topmusic.presentation.architecture.BaseActivity
import com.glowka.rafal.topmusic.presentation.architecture.FragmentNavigator
import com.glowka.rafal.topmusic.presentation.compose.initScreenData
import com.glowka.rafal.topmusic.presentation.utils.EmptyParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : BaseActivity() {

  val scope = CoroutineScope(Dispatchers.Main)
  override val navigator: FragmentNavigator by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContentView(R.layout.activity_main)
    initScreenData()

    if (savedInstanceState == null) {
      startMainFlow()
    }
    /*
     TODO: Checking if activity was no restored from state after process restart and we have
     fragment stack without proper scopes and objects in DI
     Warning !!
        It need to be solved before production release, for POC/chalange it's just not supported
        edge case.
     */
  }

  private fun startMainFlow() {
    val introFlow: IntroFlow by inject()
    introFlow.start(
      screenNavigator = navigator,
      param = EmptyParam.EMPTY,
    ) { result ->
      when (result) {
        IntroResult.Terminated -> finish()
      }
    }
  }
}