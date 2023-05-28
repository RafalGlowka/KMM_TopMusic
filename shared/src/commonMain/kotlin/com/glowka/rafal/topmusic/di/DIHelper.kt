package com.glowka.rafal.topmusic.di

import org.koin.core.Koin
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

expect fun getGlobalKoin(): Koin

inline fun <reified T : Any> inject(
  qualifier: Qualifier? = null,
  mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
  noinline parameters: ParametersDefinition? = null
): Lazy<T> =
  lazy(mode) {
    getGlobalKoin().get<T>(qualifier, parameters)
  }

inline fun <reified T : Any> injectOrNull(
  qualifier: Qualifier? = null,
  mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
  noinline parameters: ParametersDefinition? = null
): Lazy<T?> =
  lazy(mode) {
    try {
      getGlobalKoin().get<T>(qualifier, parameters)
    } catch (e: NoBeanDefFoundException) {
      null
    }
  }
