package com.glowka.rafal.topmusic.presentation

import com.glowka.rafal.topmusic.di.koinForTests
import com.glowka.rafal.topmusic.presentation.utils.CoroutineErrorHandler
import io.kotest.core.spec.style.DescribeSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
abstract class FlowSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {

  val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

  init {

    beforeSpec {
      Dispatchers.setMain(dispatcher)
      initKoinForViewModelTest()
    }

    afterSpec {
      Dispatchers.resetMain()
      cleanUpKoin()
    }
  }

  open fun Module.prepareKoinContext() {}

  private fun initKoinForViewModelTest() {

    koinForTests = startKoin {
      modules(listOf<Module>(
        module {
          single<CoroutineErrorHandler> {
            CoroutineErrorHandler()
          }
          prepareKoinContext()
        }
      ))
      createEagerInstances()
    }.koin
  }

  private fun cleanUpKoin() {
    stopKoin()
  }
}


